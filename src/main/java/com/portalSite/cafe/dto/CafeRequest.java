package com.portalSite.cafe.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CafeRequest(
        @NotNull(message = "카페이름이 비어있습니다.")
        @Size(min = 1,max = 25,message = "최대25자를 초과했습니다.")
        String cafeName,
        String description
) {
}
