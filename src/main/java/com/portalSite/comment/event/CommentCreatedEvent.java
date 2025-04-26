package com.portalSite.comment.event;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentCreatedEvent {
    private final CafeMember member;
    private final CafePost post;
    private final Comment comment;

    public CommentCreatedEvent(CafeMember member, CafePost post, Comment comment) {
        this.member = member;
        this.post = post;
        this.comment = comment;
    }
}
