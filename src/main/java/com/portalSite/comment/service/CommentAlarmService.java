package com.portalSite.comment.service;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.repository.CafeMemberRepository;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.comment.dto.request.CommentAlarmRequest;
import com.portalSite.comment.entity.Comment;
import com.portalSite.comment.entity.CommentAlarm;
import com.portalSite.comment.repository.CommentAlarmRepository;
import java.util.List;

import com.portalSite.common.exception.core.NotFoundException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentAlarmService {

    private final CommentAlarmRepository commentAlarmRepository;
    private final CafePostRepository cafePostRepository;
    private final CafeMemberRepository cafeMemberRepository;

    @Transactional
    public void sendAlarm(Member subscriber, Comment newComment) {
        System.out.println("새로운 댓글 알림: " + newComment.getContent());
        /*TODO 알림 전송 매체 선택 후 개선 필요*/
        //매개변수 subscriber는 추후 알림을 받을 사람을 특정하기 위해 필요함
    }

    public List<CommentAlarm> findSubscribeMember(Long cafePostId){
        return commentAlarmRepository.findByCafePostId(cafePostId);
    }

    @Transactional
    public void registerAlarm(Long cafeMemberId,Long cafePostId) {
        CafePost cafePost = cafePostRepository.findById(cafePostId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_POST_NOT_FOUND));
        CafeMember cafeMember = cafeMemberRepository.findById(cafeMemberId).orElseThrow(() -> new NotFoundException(ErrorCode.CAFE_MEMBER_NOT_FOUND));
        CommentAlarm alarm = CommentAlarm.of(cafeMember, cafePost);
        commentAlarmRepository.save(alarm);
    }

    @Transactional
    public void cancelAlarm(Long cafeMemberId,Long cafePostId) {
        CommentAlarm alarm = commentAlarmRepository.findByCafeMemberIdAndCafePostId(cafeMemberId, cafePostId)
                .orElseThrow(() -> new IllegalArgumentException("신청된 알람이 없습니다."));
        commentAlarmRepository.delete(alarm);
    }
}
