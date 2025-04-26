package com.portalSite.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateBlogPostRequest {

    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    private final String title;
    private final String description;
    private final List<HashtagRequest> hashtags;
}
