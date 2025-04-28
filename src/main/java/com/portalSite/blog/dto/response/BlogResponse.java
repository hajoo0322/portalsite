package com.portalSite.blog.dto.response;


import com.portalSite.blog.entity.Blog;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogResponse {

    private final Long id;
    private final Long memberId;
    private final String name;
    private final String description;

    public static BlogResponse from(Blog blog){
        return new BlogResponse(blog.getId(),blog.getMember().getId(), blog.getBlogName(), blog.getDescription());
    }

}
