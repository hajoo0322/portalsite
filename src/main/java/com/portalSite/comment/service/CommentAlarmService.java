package com.portalSite.comment.service;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.comment.dto.request.CommentAlarmRequest;
import com.portalSite.comment.entity.CommentAlarm;
import com.portalSite.comment.event.CommentCreatedEvent;
import com.portalSite.comment.repository.CommentAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentAlarmService {

    private final CommentAlarmRepository commentAlarmRepository;

    @Transactional
    public void sendAlarm(CommentCreatedEvent event) {
        CafePost cafePost = event.getPost();

        List<CommentAlarm> alarms = commentAlarmRepository.findByCafePostAndCafeMemberNot(
                cafePost, event.getMember()
        );

        for (CommentAlarm alarm : alarms) {
            System.out.println("🔔 알람 전송 대상: " + alarm.getCafeMember().getNickname());
        }
    }

    @Transactional
    public void registerAlarm(CommentAlarmRequest request) {
        CommentAlarm alarm = CommentAlarm.of(request.cafeMember(), request.cafePost());
        commentAlarmRepository.save(alarm);
    }

    @Transactional
    public void cancelAlarm(CommentAlarmRequest request) {
        CommentAlarm alarm = commentAlarmRepository.findByCafeMemberAndCafePost(request.cafeMember(), request.cafePost())
                .orElseThrow(() -> new IllegalArgumentException("신청된 알람이 없습니다."));
        commentAlarmRepository.delete(alarm);
    }
}
