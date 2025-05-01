package com.portalSite.auth;

import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import com.portalSite.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        if (memberRepository.existsByLoginId(request.loginId())){
            throw new CustomException(ErrorCode.ID_ALREADY_EXIST);
        }
        if (memberRepository.existsByEmail(request.email())){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
        }
        Member member = Member.of(request, passwordEncoder.encode(request.password()));
        memberRepository.save(member);
        String rawToken = jwtUtil.createToken(member.getId(), member.getMemberRole());
        return jwtUtil.removePrefix(rawToken);
    }

    public String login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_UNAUTHORIZED));
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_UNAUTHORIZED);
        }
        String rawToken = jwtUtil.createToken(member.getId(), member.getMemberRole());
        return jwtUtil.removePrefix(rawToken);
    }
}
