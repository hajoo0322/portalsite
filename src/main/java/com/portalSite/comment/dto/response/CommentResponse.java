package com.portalSite.comment.dto.response;

import com.portalSite.comment.entity.Comment;
import com.portalSite.comment.entity.PostType;

import java.time.LocalDateTime;

public record CommentResponse(
        PostType postType,
        Long id,
        Long memberId,
        String memberName,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getPostType(),
                comment.getId(),
                comment.getMember().getId(),
                comment.getMember().getName(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
