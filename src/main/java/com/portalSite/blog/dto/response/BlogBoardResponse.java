package com.portalSite.blog.dto.response;

import com.portalSite.blog.entity.BlogBoard;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogBoardResponse {

    private final Long id;
    private final Long blogId;
    private final String category;

    public static BlogBoardResponse from(BlogBoard blogBoard) {
        return new BlogBoardResponse(blogBoard.getId(), blogBoard.getBlog().getId(), blogBoard.getCategory());
    }
}
