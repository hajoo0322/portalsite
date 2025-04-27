package com.portalSite.blog.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateBlogPostRequest {

    private final Long blogBoardId;
    private final String title;
    private final String description;
    private final List<String> hashtags;
}
