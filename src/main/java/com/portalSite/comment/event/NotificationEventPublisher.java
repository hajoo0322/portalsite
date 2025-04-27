package com.portalSite.comment.event;

import com.portalSite.cafe.entity.CafePost;
import com.portalSite.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publishNewCommentEvent(CafePost post, Comment comment) {
        publisher.publishEvent(new CommentCreatedEvent(post, comment));
    }
}
