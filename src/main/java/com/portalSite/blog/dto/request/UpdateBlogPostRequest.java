package com.portalSite.blog.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = UpdateBlogPostRequest.UpdateBlogPostRequestBuilder.class)
public class UpdateBlogPostRequest {

    private final Long blogBoardId;
    private final String title;
    private final String description;

    @NotNull
    private final List<String> hashtags;

    public static class UpdateBlogPostRequestBuilder {

        @JsonProperty("blogBoardId")
        private Long blogBoardId;

        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private String description;

        @JsonProperty("hashtags")
        private List<String> hashtags;
    }
}
