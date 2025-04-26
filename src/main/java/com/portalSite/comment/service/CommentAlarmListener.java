package com.portalSite.comment.service;

import com.portalSite.comment.event.CommentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentAlarmListener {
    private final CommentAlarmService commentAlarmService;

    @EventListener
    public void handleCommentCreated(CommentCreatedEvent event) {
        commentAlarmService.sendAlarm(event);
    }
}
