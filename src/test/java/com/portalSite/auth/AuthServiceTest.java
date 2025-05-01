package com.portalSite.auth;

import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import com.portalSite.mock.MockMemberFactory;
import com.portalSite.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
                "user@example.com", "userLogin01", "userpassword12",
                "유저", "01012345678", "선량한시민"
        );
        loginRequest = new LoginRequest("userLogin01", "userpassword12");
    }

    @Test
    void register_정상작동() {
        when(memberRepository.existsByLoginId(registerRequest.loginId())).thenReturn(false);
        when(memberRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPw");
        when(jwtUtil.createToken(any(), any())).thenReturn("Bearer testToken");
        when(jwtUtil.removePrefix("Bearer testToken")).thenReturn("testToken");

        String token = authService.register(registerRequest);

        assertThat(token).isEqualTo("testToken");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void register_중복_loginId_예외() {
        when(memberRepository.existsByLoginId(registerRequest.loginId())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.ID_ALREADY_EXIST.getMessage());
    }

    @Test
    void register_중복_email_예외() {
        when(memberRepository.existsByLoginId(registerRequest.loginId())).thenReturn(false);
        when(memberRepository.existsByEmail(registerRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.EMAIL_ALREADY_EXIST.getMessage());
    }

    @Test
    void login_정상작동() {
        Member mockMember = MockMemberFactory.createUser(1L);
        when(memberRepository.findByLoginId(loginRequest.loginId())).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(loginRequest.password(), mockMember.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(any(), any())).thenReturn("Bearer token");
        when(jwtUtil.removePrefix("Bearer token")).thenReturn("token");

        String token = authService.login(loginRequest);

        assertThat(token).isEqualTo("token");
    }

    @Test
    void login_아이디없음_예외() {
        when(memberRepository.findByLoginId(loginRequest.loginId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.LOGIN_UNAUTHORIZED.getMessage());
    }

    @Test
    void login_비밀번호불일치_예외() {
        Member mockMember = MockMemberFactory.createUser(1L);
        when(memberRepository.findByLoginId(loginRequest.loginId())).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.LOGIN_UNAUTHORIZED.getMessage());
    }
}
