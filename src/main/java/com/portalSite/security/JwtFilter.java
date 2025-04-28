package com.portalSite.security;

import com.portalSite.member.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                log.error("만료된 JWT 토큰입니다.");
                //TODO exception
                return;
            } catch (SecurityException | MalformedJwtException e) {
                log.error("유효하지 않은 JWT 서명입니다.");
                //exception
                return;
            } catch (UnsupportedJwtException e) {
                log.error("지원되지 않는 JWT 토큰입니다.");
                //exception
                return;
            } catch (IllegalArgumentException e) {
                log.error("잘못된 JWT 토큰 형식입니다.");
                //exception
                return;
            } catch (Exception e) {
                log.error("예기치 못한 오류.");
                //exception
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