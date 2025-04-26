package com.portalSite.blog.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HashtagRequest {

    @NotNull
    private final List<String> hashtags;
}
