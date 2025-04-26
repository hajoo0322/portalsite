package com.portalSite.comment.service;

import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.repository.CafeRepository;
import com.portalSite.comment.dto.request.CommentRequest;
import com.portalSite.comment.dto.response.CommentResponse;
import com.portalSite.comment.entity.Comment;
import com.portalSite.comment.repository.CommentRepository;
import com.portalSite.member.entity.Member;
import com.portalSite.news.entity.News;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final CafeRepository cafeRepository;
    private final NewsRepository newsRepository;

    @Transactional
    public CommentResponse createComment(CommentRequest commentRequest, Member member) {
        Comment comment = Comment.of(member, commentRequest.blog(), commentRequest.news(),
                commentRequest.cafe(), commentRequest.content());
        commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentOfPost(String type, Long postId) {
        List<Comment> commentList;

        if (type.equals("blog")) {
            Blog blog = blogRepository.findById(postId);
            commentList = commentRepository.findAllByBlog(blog);
        } else if (type.equals("cafe")) {
            Cafe cafe = cafeRepository.findById(postId);
            commentList = commentRepository.findAllByCafe(cafe);
        } else if (type.equals("news")) {
            News news = newsRepository.findById(postId);
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
