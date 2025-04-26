package com.portalSite.comment.repository;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.comment.entity.CommentAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentAlarmRepository extends JpaRepository<CommentAlarm, Long> {
    List<CommentAlarm> findByCafePostAndCafeMemberNot(CafePost post, CafeMember member);
    Optional<CommentAlarm> findByCafeMemberAndCafePost(CafeMember member, CafePost post);
}
