package com.portalSite.comment.controller;

import com.portalSite.comment.dto.request.CommentRequest;
import com.portalSite.comment.dto.response.CommentResponse;
import com.portalSite.comment.entity.PostType;
import com.portalSite.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/{memberId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @PathVariable Long memberId,
            @RequestBody CommentRequest commentRequest,
            @RequestParam(name = "type")PostType type
            ) {
        CommentResponse response = commentService.createComment(type, postId, memberId, commentRequest);
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

    @GetMapping("/members/{memberId}")
    public ResponseEntity<List<CommentResponse>> getCommentsOfMember(
            @PathVariable Long memberId
    ) {
        List<CommentResponse> responseList = commentService.getCommentOfMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request
    ) {
        CommentResponse response = commentService.updateComment(commentId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
