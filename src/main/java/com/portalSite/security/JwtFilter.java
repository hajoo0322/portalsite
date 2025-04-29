package com.portalSite.security;

import com.portalSite.common.exception.core.ErrorResponseHandler;
import com.portalSite.member.entity.MemberRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtSecurityProperties jwtSecurityProperties;
    private final ErrorResponseHandler errorResponseHandler;
    private final JwtUtil jwtUtil;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return jwtSecurityProperties.secret().whiteList().stream()
                .anyMatch(whitelist -> antPathMatcher.match(whitelist, uri));
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws IOException, ServletException {
        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt != null && bearerJwt.startsWith(jwtSecurityProperties.token().prefix())) {
            String token = jwtUtil.substringToken(bearerJwt);
            try {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(token);
                }
            } catch (ExpiredJwtException e) {
                errorResponseHandler.send(response, HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.");
                return;
            } catch (SignatureException e) {
                errorResponseHandler.send(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다.");
                return;
            } catch (SecurityException | MalformedJwtException e) {
                errorResponseHandler.send(response, HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰 형식입니다.");
                return;
            } catch (UnsupportedJwtException e) {
                errorResponseHandler.send(response, HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
                return;
            } catch (IllegalArgumentException e) {
                errorResponseHandler.send(response, HttpStatus.BAD_REQUEST, e.getMessage());
                return;
            } catch (JwtException e) {
                errorResponseHandler.send(response, HttpStatus.UNAUTHORIZED, "예상치 못한 JWT 토큰 오류: " + e.getMessage());
                return;
            } catch (Exception e) {
                errorResponseHandler.send(response, HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버 오류: " + e.getMessage());
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Context 에 인증정보 저장
     */
    private void setAuthentication(String token) {
        Claims claims = jwtUtil.extractClaims(token);
        String[] data = claims.getSubject().split(":");
        Long memberId = Long.valueOf(data[0]);
        MemberRole userRole = MemberRole.of(data[1]);

        AuthUser authUser = AuthUser.of(memberId, userRole);
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser, token);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}