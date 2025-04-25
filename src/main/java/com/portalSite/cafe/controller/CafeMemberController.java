package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.CafeMemberRequest;
import com.portalSite.cafe.dto.CafeMemberResponse;
import com.portalSite.cafe.service.CafeMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafes/{cafeId}/cafe-member")
@RequiredArgsConstructor
public class CafeMemberController {

    private final CafeMemberService cafeMemberService;

    @PostMapping("/members/{memberId}")
    public ResponseEntity<CafeMemberResponse> addCafeMember(@RequestBody @Valid CafeMemberRequest cafeMemberRequest, @PathVariable Long memberId) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.addCafeMember(cafeMemberRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cafeMemberResponse);
    }

    @GetMapping("/{cafeMemberId}")
    public ResponseEntity<CafeMemberResponse> getCafeMember(@PathVariable Long cafeMemberId) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.getCafeMember(cafeMemberId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeMemberResponse);
    }

    @PatchMapping("/{cafeMemberId}")
    public ResponseEntity<CafeMemberResponse> updateCafeMember(@RequestBody @Valid CafeMemberRequest cafeMemberRequest, @PathVariable Long cafeMemberId) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.updateCafeMember(cafeMemberRequest, cafeMemberId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeMemberResponse);
    }

    @DeleteMapping("/{cafeMemberId}")
    public ResponseEntity<Void> deleteCafeMember(@PathVariable Long cafeMemberId) {
        cafeMemberService.deleteCafeMember(cafeMemberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
