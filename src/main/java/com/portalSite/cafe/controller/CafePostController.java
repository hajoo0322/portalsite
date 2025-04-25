package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.CafePostRequest;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.service.CafePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CafePostController {

    private final CafePostService cafePostService;

    @PostMapping("/cafes/{cafeId}/members/{memberId}/cafe-boards/{cafeBoardId}/cafe-posts")
    public ResponseEntity<CafePostResponse> addCafePost(@RequestBody CafePostRequest cafePostRequest, @PathVariable Long cafeId, @PathVariable Long cafeBoardId,@PathVariable Long memberId) {
        CafePostResponse cafePostResponse = cafePostService.addCafePost(cafePostRequest, cafeId, cafeBoardId,memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cafePostResponse);
    }

    @GetMapping("/cafes/{cafeId}/cafe-posts")
    public ResponseEntity<List<CafePostResponse>> getAllCafePostByCafeId(@PathVariable Long cafeId) {
        List<CafePostResponse> cafePostResponseList = cafePostService.getAllCafePostByCafeId(cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafePostResponseList);
    }

    @PatchMapping("/cafe-posts/{cafePostId}")
    public ResponseEntity<CafePostResponse> updateCafePost(@RequestBody CafePostRequest cafePostRequest, @PathVariable Long cafePostId) {
        CafePostResponse cafePostResponse = cafePostService.updateCafePost(cafePostRequest, cafePostId);
        return ResponseEntity.status(HttpStatus.OK).body(cafePostResponse);
    }

    @DeleteMapping("/cafe-posts/{cafePostId}")
    public ResponseEntity<Void> deleteCafePost(@PathVariable Long cafePostId) {
        cafePostService.deleteCafePost(cafePostId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
