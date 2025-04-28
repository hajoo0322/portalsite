package com.portalSite.like.controller;

import com.portalSite.like.service.LikeService;
import com.portalSite.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/cafes/{cafeId}/cafePosts/{cafePostId}")
    public ResponseEntity<Void> addLike(
            @PathVariable Long cafeId,
            @PathVariable Long cafePostId,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        likeService.doLike(cafeId,cafePostId, authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/cafePosts/{cafePostId}")
    public ResponseEntity<Integer> getLikes(
            @PathVariable Long cafePostId
    ) {
        Integer response = likeService.countLikes(cafePostId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> deleteLike(
            @PathVariable Long likeId
    ) {
        likeService.undoLike(likeId);
        return ResponseEntity.status(HttpStatus.OK).build(); // 여기도 no content
    }
}
