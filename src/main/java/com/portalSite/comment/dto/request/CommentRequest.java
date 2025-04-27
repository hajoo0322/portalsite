package com.portalSite.comment.dto.request;

import com.portalSite.blog.entity.Blog;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.news.entity.News;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        Blog blog,
        News news,
        CafePost cafePost,

        @NotNull
        @Size(min = 1, max = 255, message = "댓글은 최대 255글자까지 작성 가능합니다.")
        String content
) {
}
