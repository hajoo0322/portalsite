package com.portalSite.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateBlogPostRequest(
    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    String title,
    String description,
    @NotNull
    List<String> hashtags
) {
}
