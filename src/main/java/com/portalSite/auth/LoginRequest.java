package com.portalSite.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "아이디를 입력해주세요")
        String loginId,

        @NotBlank(message = "패스워드를 입력해주세요")
        String password
) {
}
