package com.portalSite.blog.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record HashtagRequest(
    @NotNull List<String> hashtags
) {
}
