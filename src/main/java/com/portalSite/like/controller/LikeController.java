package com.portalSite.like.controller;

import com.portalSite.like.entity.Like;
import com.portalSite.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/cafePosts/{cafePostId}/members/{membersId}")
    public ResponseEntity<Void> addLike(
            @PathVariable Long cafePostId,
            @PathVariable Long membersId
    ) {
        likeService.doLike(cafePostId, membersId);
        return ResponseEntity.status(HttpStatus.OK).build(); // ok 보단 created 가맞지않나?
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
