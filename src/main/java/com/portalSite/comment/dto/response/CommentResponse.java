package com.portalSite.comment.dto.response;

import com.portalSite.comment.entity.Comment;
import com.portalSite.comment.entity.PostType;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long memberId,
        String memberName,
        PostType postType,
        Long postId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getMember().getId(),
                comment.getMember().getName(),
                comment.getPostType(),
                comment.getPostId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
