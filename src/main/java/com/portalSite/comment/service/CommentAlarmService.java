package com.portalSite.comment.service;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.comment.dto.request.CommentAlarmRequest;
import com.portalSite.comment.entity.Comment;
import com.portalSite.comment.entity.CommentAlarm;
import com.portalSite.comment.repository.CommentAlarmRepository;
import java.util.List;

import com.portalSite.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentAlarmService {

    private final CommentAlarmRepository commentAlarmRepository;

    @Transactional
    public void sendAlarm(Member subscriber, Comment newComment) {
        System.out.println("새로운 댓글 알림: " + newComment.getContent());
        /*TODO 알림 전송 매체 선택 후 개선 필요*/
        //매개변수 subscriber는 추후 알림을 받을 사람을 특정하기 위해 필요함
    }

    public List<CafeMember> findSubscribeMember(Long cafePostId){
        return commentAlarmRepository.findByCafePostId(cafePostId);
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
