package com.portalSite.blog.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateBlogPostRequest(
    Long blogBoardId,
    String title,
    String description,
    @NotNull List<String> hashtags
) {
    @JsonCreator
    public UpdateBlogPostRequest(
        @JsonProperty("blogBoardId") Long blogBoardId,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("hashtags") List<String> hashtags
    ) {
        this.blogBoardId = blogBoardId;
        this.title = title;
        this.description = description;
        this.hashtags = hashtags;
    }
}
