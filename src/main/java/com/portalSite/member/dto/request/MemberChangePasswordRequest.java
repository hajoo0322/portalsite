package com.portalSite.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberChangePasswordRequest(
        String oldPassword,

        @NotBlank(message = "패스워드를 입력해주세요")
        @Size(min = 12, max = 20, message = "비밀번호는 12에서 20자 사이로 입력해주세요.")
        String newPassword
) {
}
