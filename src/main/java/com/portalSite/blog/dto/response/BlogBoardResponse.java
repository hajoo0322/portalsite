package com.portalSite.blog.dto.response;

import com.portalSite.blog.entity.BlogBoard;

public record BlogBoardResponse(
    Long id,
    Long blogId,
    String category
) {
    public static BlogBoardResponse from(BlogBoard blogBoard) {
        return new BlogBoardResponse(blogBoard.getId(), blogBoard.getBlog().getId(),
            blogBoard.getCategory());
    }
}
