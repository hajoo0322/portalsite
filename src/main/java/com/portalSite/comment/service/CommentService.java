package com.portalSite.comment.service;

import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.comment.dto.request.CommentRequest;
import com.portalSite.comment.dto.response.CommentResponse;
import com.portalSite.comment.entity.Comment;
import com.portalSite.comment.entity.PostType;
import com.portalSite.comment.event.CommentCreatedEvent;
import com.portalSite.comment.repository.CommentRepository;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CafePostRepository cafePostRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CommentResponse createComment(PostType postType, Long postId, Long memberId, CommentRequest request) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException(""));
        Comment comment = Comment.of(foundMember, postType, postId, request.content());

        commentRepository.save(comment);
        if (postType == PostType.CAFE) {
            CafePost foundCafePost = cafePostRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
            eventPublisher.publishEvent(new CommentCreatedEvent(foundCafePost, comment)); // 댓글 저장후 알림 발송
        }

        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentOfPost(PostType type, Long postId) {
        List<Comment> comments = commentRepository.findAllByPostTypeAndPostId(type, postId);

        return comments.stream().map(CommentResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentOfMember(Long memberId) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException(""));
        return commentRepository.findAllByMember(foundMember).stream().
                map(CommentResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest, Long memberId) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow();
        validateMember(memberId, foundComment);
        foundComment.updateContent(commentRequest.content());
        return CommentResponse.from(foundComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow();
        validateMember(memberId, foundComment);
        commentRepository.delete(foundComment);
    }

    private void validateMember(Long memberId, Comment comment) {
        if (!comment.getMember().getId().equals(memberId)) {
            throw new RuntimeException("");
        }
    }
}
