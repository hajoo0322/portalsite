package com.portalSite.comment.event;

import com.portalSite.cafe.entity.CafePost;
import com.portalSite.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentCreatedEvent {
    private final CafePost post;
    private final Comment comment;

    public CommentCreatedEvent(CafePost post, Comment comment) {
        this.post = post;
        this.comment = comment;
    }
}
