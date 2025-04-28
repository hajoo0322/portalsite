package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.CafeMemberRequest;
import com.portalSite.cafe.dto.CafeMemberResponse;
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
@RequestMapping("/cafes/{cafeId}/cafe-members")
@RequiredArgsConstructor
public class CafeMemberController {

    private final CafeMemberService cafeMemberService;

    @PostMapping
    public ResponseEntity<CafeMemberResponse> addCafeMember(
            @RequestBody @Valid CafeMemberRequest cafeMemberRequest,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long cafeId) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.addCafeMember(cafeMemberRequest, authUser.memberId(),cafeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cafeMemberResponse);
    }

    @GetMapping
    public ResponseEntity<CafeMemberResponse> getCafeMember(
            @PathVariable Long cafeId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.getCafeMember(authUser.memberId(), cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeMemberResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CafeMemberResponse>> getAllCafeMember(@PathVariable Long cafeId) {
        List<CafeMemberResponse> cafeMemberResponseList = cafeMemberService.getAllCafeMember(cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeMemberResponseList);
    }

    @PatchMapping
    public ResponseEntity<CafeMemberResponse> updateCafeMember(
            @RequestBody @Valid CafeMemberRequest cafeMemberRequest,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long cafeId) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.updateCafeMember(cafeMemberRequest, authUser.memberId(), cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeMemberResponse);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteCafeMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long cafeId) {
        cafeMemberService.deleteCafeMember(authUser.memberId(), cafeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
