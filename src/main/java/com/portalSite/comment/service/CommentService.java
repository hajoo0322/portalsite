package com.portalSite.comment.service;

import com.portalSite.blog.entity.Blog;
import com.portalSite.blog.repository.BlogRepository;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.comment.dto.request.CommentRequest;
import com.portalSite.comment.dto.response.CommentResponse;
import com.portalSite.comment.entity.Comment;
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
    private final BlogRepository blogRepository;
    private final CafePostRepository cafePostRepository;
    private final NewsRepository newsRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResponse createComment(CommentRequest commentRequest, Long memberId) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException(""));
        Comment comment = Comment.of(foundMember, commentRequest.blog(), commentRequest.news(),
                commentRequest.cafePost(), commentRequest.content());

        commentRepository.save(comment);

        eventPublisher.publishEvent(new CommentCreatedEvent(comment.getCafePost(), comment)); // 댓글 저장후 알림 발송

        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentOfPost(String type, Long postId) {
        List<Comment> commentList;

        if (type.equals("blog")) {
            Blog blog = blogRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
            commentList = commentRepository.findAllByBlog(blog);
        } else if (type.equals("cafe")) {
            CafePost cafePost = cafePostRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
            commentList = commentRepository.findAllByCafePost(cafePost);
        } else if (type.equals("news")) {
            News news = newsRepository.findById(postId).orElseThrow(
                    () -> new RuntimeException(""));
            commentList = commentRepository.findAllByNews(news);
        } else {
            throw new RuntimeException("하나 이상의 게시글 정보를 입력해주세요");
        }

        List<CommentResponse> responseList = commentList.stream().
                map(CommentResponse::from).collect(Collectors.toList());
        return responseList;
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
