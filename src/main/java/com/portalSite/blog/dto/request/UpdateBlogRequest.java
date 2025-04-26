package com.portalSite.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateBlogRequest {

    @NotBlank(message = "블로그명은 비워둘 수 없습니다.")
    private final String name;
    private final String description;
}
