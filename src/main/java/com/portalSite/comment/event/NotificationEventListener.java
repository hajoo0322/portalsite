package com.portalSite.comment.event;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.comment.service.CommentAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final CommentAlarmService commentAlarmService;

    @EventListener
    public void handleNewCommentNotification(CommentCreatedEvent event) {

        List<CafeMember> subscriberList = commentAlarmService.findSubscribeMember(event.getPost().getId());

        for (CafeMember subscriber : subscriberList) {
            commentAlarmService.sendAlarm(subscriber.getMember(), event.getComment());
        }
    }
}
