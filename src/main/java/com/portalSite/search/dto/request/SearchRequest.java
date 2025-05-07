package com.portalSite.search.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SearchRequest(
        @NotBlank(message = "검색어를 입력해주세요.")
        @Size(min = 2, message = "검색어는 2글자 이상 입력해주세요")
        String keyword
) {
}
