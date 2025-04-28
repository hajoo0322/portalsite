package com.portalSite.comment.controller;

import com.portalSite.comment.dto.request.CommentRequest;
import com.portalSite.comment.dto.response.CommentResponse;
import com.portalSite.comment.entity.PostType;
import com.portalSite.comment.service.CommentService;
import com.portalSite.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest commentRequest,
            @RequestParam(name = "type")PostType type,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        CommentResponse response = commentService.createComment(type, postId, authUser.memberId(), commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsOfPost(
            @PathVariable Long postId,
            @RequestParam(name = "type") PostType type
            ){
        List<CommentResponse> responseList = commentService.getCommentOfPost(type, postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/members")
    public ResponseEntity<List<CommentResponse>> getCommentsOfMember(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<CommentResponse> responseList = commentService.getCommentOfMember(authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        CommentResponse response = commentService.updateComment(commentId, request, authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        commentService.deleteComment(commentId, authUser.memberId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
