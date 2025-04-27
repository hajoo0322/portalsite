package com.portalSite.news.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NewsCategoryRequest(
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    String name,
    Long parentId
) {

}
