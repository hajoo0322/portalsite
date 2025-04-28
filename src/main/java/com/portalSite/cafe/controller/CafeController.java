package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.*;
import com.portalSite.cafe.service.CafeMemberService;
import com.portalSite.cafe.service.CafeService;
import com.portalSite.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafes")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;
    private final CafeMemberService cafeMemberService;

    @PostMapping
    public ResponseEntity<CafeAndCafeMemberResponse> addCafe(
            @RequestBody @Valid CafeAndCafeMemberRequest cafeAndCafeMemberRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        CafeResponse cafeResponse = cafeService.addCafe(cafeAndCafeMemberRequest.cafeRequest());
        CafeMemberResponse cafeMemberResponse = cafeMemberService.addFirstCafeMember(authUser.memberId(),cafeResponse.id(),cafeAndCafeMemberRequest.cafeMemberRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(CafeAndCafeMemberResponse.from(cafeResponse,cafeMemberResponse));
    }

    @GetMapping("/{cafeId}")
    public ResponseEntity<CafeResponse> getCafe(@PathVariable Long cafeId) {
        CafeResponse cafeResponse = cafeService.getCafe(cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeResponse);
    }

    @PatchMapping("/{cafeId}")
    public ResponseEntity<CafeResponse> updateCafe(@RequestBody @Valid CafeRequest requestCafe, @PathVariable Long cafeId) {
        CafeResponse cafeResponse = cafeService.updateCafe(requestCafe, cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeResponse);
    }

    @DeleteMapping("/{cafeId}")
    public ResponseEntity<Void> deleteCafe(@PathVariable Long cafeId) {
        cafeService.deleteCafe(cafeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
