package com.portalSite.like.controller;

import com.portalSite.like.service.LikeService;
import com.portalSite.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/cafes/{cafeId}/cafePosts/{cafePostId}/likes")//카페글에만 좋아요? 가능함 개선필요
    public ResponseEntity<Void> addLike(
            @PathVariable Long cafeId,
            @PathVariable Long cafePostId,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        likeService.doLike(cafeId,cafePostId, authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/cafePosts/{cafePostId}/likes")
    public ResponseEntity<Integer> getLikes(
            @PathVariable Long cafePostId
    ) {
        Integer response = likeService.countLikes(cafePostId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/likes/{likeId}")// 카페글에만 마찬가지 그리고 게시글+자기자신의 id 를 통해서 삭제하도록해야할듯
    public ResponseEntity<Void> deleteLike(
            @PathVariable Long likeId
    ) {
        likeService.undoLike(likeId);
        return ResponseEntity.status(HttpStatus.OK).build(); // 여기도 no content
    }
}
