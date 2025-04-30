package com.portalSite.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberRestoreRequest(
        @NotBlank(message = "이메일을 입력해주세요")
        String email,

        @NotBlank(message = "비밀번호 입력해주세요")
        String password,

        @NotBlank(message = "이름을 입력해주세요")
        String name,

        @NotBlank(message = "전화번호를 입력해주세요")
        String phoneNumber
) {
}
