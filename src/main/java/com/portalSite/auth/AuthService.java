package com.portalSite.auth;

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
            throw new RuntimeException("중복 id");
        }
        if (memberRepository.existsByEmail(request.email())){
            throw new RuntimeException("중복 이메일");
        }
        Member member = Member.of(request, passwordEncoder.encode(request.password()));
        memberRepository.save(member);
        String rawToken = jwtUtil.createToken(member.getId(), member.getMemberRole());
        return jwtUtil.removePrefix(rawToken);
    }

    public String login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new RuntimeException("아이디나 비밀번호가 올바르지 않습니다."));
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new RuntimeException("아이디나 비밀번호가 올바르지 않습니다.");
        }
        String rawToken = jwtUtil.createToken(member.getId(), member.getMemberRole());
        return jwtUtil.removePrefix(rawToken);
    }
}
