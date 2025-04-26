package com.portalSite.comment.dto.response;

import com.portalSite.cafe.entity.Cafe;
import com.portalSite.comment.entity.Comment;
import com.portalSite.member.entity.Member;
import com.portalSite.news.entity.News;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Member member,
        Blog blog,
        News news,
        Cafe cafe,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse of(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getMember(),
                comment.getBlog(),
                comment.getNews(),
                comment.getCafe(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
