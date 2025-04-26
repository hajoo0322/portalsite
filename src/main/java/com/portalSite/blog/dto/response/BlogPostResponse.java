package com.portalSite.blog.dto.response;

import com.portalSite.blog.entity.BlogPost;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogPostResponse {

    private final Long id;
    private final Long memberId;
    private final String title;
    private final String description;

    public static BlogResponse from(BlogPost post){
        return new BlogResponse(post.getId(),post.getMember().getId(), post.getTitle(),
            post.getDescription());
    }

}
