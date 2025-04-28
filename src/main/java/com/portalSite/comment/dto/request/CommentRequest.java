package com.portalSite.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotNull
        @Size(min = 1, max = 255, message = "댓글은 최대 255글자까지 작성 가능합니다.")
        String content
) {
}
