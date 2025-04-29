package com.portalSite.blog.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateBlogRequest(
    String name,
    String description
) {
    @JsonCreator
    public UpdateBlogRequest(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description
    ) {
        this.name = name;
        this.description = description;
    }
}
