package com.portalSite.comment.service;

import com.portalSite.blog.entity.BlogPost;
import com.portalSite.blog.repository.BlogPostRepository;
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
import com.portalSite.news.entity.News;
import com.portalSite.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ApplicationEventPublisher eventPublisher;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BlogPostRepository blogPostRepository;
    private final NewsRepository newsRepository;
    private final CafePostRepository cafePostRepository;

    @Transactional
    public CommentResponse createComment(PostType postType, Long postId, Long memberId, CommentRequest request) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException(""));
        Comment comment;

        switch (postType) {
            case BLOG -> {
                BlogPost foundBlogPost = blogPostRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
                comment = Comment.of(foundMember, foundBlogPost, request.content(), postType);
            }
            case CAFE -> {
                CafePost foundCafePost = cafePostRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
                comment = Comment.of(foundMember, foundCafePost, request.content(), postType);
            }
            case NEWS -> {
                News foundNews = newsRepository.findById(postId).orElseThrow(
                        () -> new RuntimeException(""));
                comment = Comment.of(foundMember, foundNews, request.content(), postType);
            }
            default -> throw new RuntimeException("");
        }

        commentRepository.save(comment);
        eventPublisher.publishEvent(new CommentCreatedEvent(comment.getCafePost(), comment)); // 댓글 저장후 알림 발송

        return CommentResponse.from(comment, postType);
    }


    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentOfPost(PostType type, Long postId) {
        List<Comment> comments;

        if (type == PostType.BLOG) {
            BlogPost blogPost = blogPostRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
            comments = commentRepository.findAllByBlogPost(blogPost);
        } else if (type == PostType.CAFE) {
            CafePost cafePost = cafePostRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
            comments = commentRepository.findAllByCafePost(cafePost);
        } else if (type == PostType.NEWS) {
            News news = newsRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
            comments = commentRepository.findAllByNews(news);
        } else {
            throw new RuntimeException("");
        }

        return comments.stream().
                map(comment -> CommentResponse.from(comment, type)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentOfMember(Long memberId) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException(""));
        return commentRepository.findAllByMember(foundMember).stream().
                map(comment -> CommentResponse.from(comment, )).collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow();
        foundComment.setContent(commentRequest.content());
        return CommentResponse.from(foundComment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow();
        commentRepository.delete(foundComment);
    }
}
