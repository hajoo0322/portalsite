package com.portalSite.comment.repository;

import com.portalSite.comment.entity.Comment;
import com.portalSite.comment.entity.PostType;
import com.portalSite.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMember(Member member);
    List<Comment> findAllByPostTypeAndPostId(PostType postType, Long postId);
}
