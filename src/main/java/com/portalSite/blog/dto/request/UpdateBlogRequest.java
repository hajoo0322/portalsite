package com.portalSite.blog.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = UpdateBlogRequest.UpdateBlogRequestBuilder.class)
public class UpdateBlogRequest {

    private final String name;
    private final String description;

    public static class UpdateBlogRequestBuilder {

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

    }
}
