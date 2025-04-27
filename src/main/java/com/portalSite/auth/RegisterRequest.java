package com.portalSite.auth;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank(message = "사용하실 이메일을 입력해주세요.")
        @Email(message = "이메일 양식에 맞춰 입력해주세요.")
        String email,

        @Size(min = 10, max = 15, message = "로그인 아이디는 10에서 15자 사이로 입력해주세요.")
        String loginId,

        @NotBlank(message = "패스워드를 입력해주세요")
        @Size(min = 12, max = 20, message = "비밀번호는 12에서 20자 사이로 입력해주세요.")
        String password,

        @NotBlank(message = "이름을 입력해주세요.")
        @Size(min = 2, max = 20, message = "이름은 2~20글자 사이여야 합니다.")
        String name,

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^010\\d{8}$", message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다.")
        String phoneNumber,

        @NotBlank(message = "별명을 입력해주세요.")
        @Size(min = 4, max = 10, message = "별명은 4에서 10글자 사이의 단어여야 합니다.")
        String nickname
) {
}
