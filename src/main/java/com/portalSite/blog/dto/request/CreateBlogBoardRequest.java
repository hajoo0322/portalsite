package com.portalSite.blog.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateBlogBoardRequest(
    @NotBlank(message = "카테고리명은 비워둘 수 없습니다.")
    String category
) {
}
