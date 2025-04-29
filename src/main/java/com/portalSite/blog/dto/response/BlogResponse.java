package com.portalSite.blog.dto.response;

import com.portalSite.blog.entity.Blog;

public record BlogResponse(
    Long id,
    Long memberId,
    String name,
    String description
) {
    public static BlogResponse from(Blog blog) {
        return new BlogResponse(blog.getId(), blog.getMember().getId(), blog.getBlogName(),
            blog.getDescription());
    }
}
