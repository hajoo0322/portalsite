package com.portalSite.cafe.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CafeMemberRequest(
        @NotNull(message = "닉네임은 비어있을수 없습니다.")
        @Size(max = 12)
        String nickname
) {

}
