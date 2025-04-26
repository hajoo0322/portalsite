package com.portalSite.comment.event;

import com.portalSite.cafe.entity.CafePost;
import com.portalSite.comment.entity.Comment;
import com.portalSite.member.entity.Member;
import lombok.Getter;

@Getter
public class CommentCreatedEvent {
    private final Member member;
    private final CafePost post;
    private final Comment comment;

    public CommentCreatedEvent(Member member, CafePost post, Comment comment) {
        this.member = member;
        this.post = post;
        this.comment = comment;
    }
}
