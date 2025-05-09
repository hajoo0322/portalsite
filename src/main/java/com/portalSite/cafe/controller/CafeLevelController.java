package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.*;
import com.portalSite.cafe.service.CafeLevelService;
import com.portalSite.cafe.service.CafeMemberService;
import com.portalSite.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cafes/{cafeId}/cafe-levels")
@RequiredArgsConstructor
public class CafeLevelController {

    private final CafeLevelService cafeLevelService;
    private final CafeMemberService cafeMemberService;

    @PostMapping
    public ResponseEntity<CafeLevelAndCafeMemberResponse> addCafeLevel(
            @RequestBody @Valid CafeLevelAndCafeMemberRequest cafeLevelAndCafeMemberRequest,
            @PathVariable Long cafeId,
            @AuthenticationPrincipal AuthUser authUser) {
        List<CafeLevelResponse> cafeLevelResponses = cafeLevelService.addCafeLevel(cafeLevelAndCafeMemberRequest.cafeLevelRequestList(), cafeId);
        CafeMemberResponse cafeMemberResponse = cafeMemberService.addFirstCafeMember(authUser.memberId(),cafeId,cafeLevelAndCafeMemberRequest.cafeMemberRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(CafeLevelAndCafeMemberResponse.from(cafeLevelResponses,cafeMemberResponse));
    }

    @GetMapping
    public ResponseEntity<List<CafeLevelResponse>> getCafeLevel(@PathVariable Long cafeId) {
        List<CafeLevelResponse> cafeLevel = cafeLevelService.getCafeLevel(cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeLevel);
    }

    @PatchMapping
    public ResponseEntity<List<CafeLevelResponse>> updateCafeLevel(@RequestBody @Valid CafeLevelRequestList cafeLevelRequestList,@PathVariable Long cafeId) {
        List<CafeLevelResponse> cafeLevel = cafeLevelService.updateCafeLevel(cafeLevelRequestList,cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeLevel);
    }
}
