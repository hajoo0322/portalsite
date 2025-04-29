package com.portalSite.blog.dto.response;

import com.portalSite.blog.entity.BlogPost;
import lombok.Getter;


public record BlogPostResponse(
    Long id,
    Long memberId,
    Long blogboardId,
    String title,
    String description
) {
    public static BlogPostResponse from(BlogPost post) {
        return new BlogPostResponse(post.getId(), post.getMember().getId(),
            post.getBlogBoard().getId(), post.getTitle(), post.getDescription());
    }
}
