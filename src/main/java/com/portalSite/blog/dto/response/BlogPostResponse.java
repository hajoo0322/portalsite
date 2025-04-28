package com.portalSite.blog.dto.response;

import com.portalSite.blog.entity.BlogPost;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogPostResponse {

    private final Long id;
    private final Long memberId;
    private final Long blogboardId;
    private final String title;
    private final String description;

    public static BlogPostResponse from(BlogPost post){
        return new BlogPostResponse(post.getId(),post.getMember().getId(), post.getBlogBoard().getId(), post.getTitle(), post.getDescription());
    }

}
