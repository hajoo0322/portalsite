package com.portalSite.blog.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateBlogRequest (
    @NotBlank(message = "블로그명은 비워둘 수 없습니다.")
    String name,
    String description
){
}
