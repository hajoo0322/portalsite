package com.portalSite.cafe.dto;

import jakarta.validation.constraints.NotNull;

public record CafeLevelRequest(
        @NotNull(message = "카페등급이름이 비어있습니다.") String grade,
        String description,
        @NotNull(message = "자동등업 여부를 선택해야합니다.") Boolean autoLevel
) {
}
