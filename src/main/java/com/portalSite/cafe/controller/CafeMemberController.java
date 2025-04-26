package com.portalSite.cafe.controller;

import com.portalSite.cafe.dto.CafeMemberRequest;
import com.portalSite.cafe.dto.CafeMemberResponse;
import com.portalSite.cafe.service.CafeMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cafes/{cafeId}/cafe-members")
@RequiredArgsConstructor
public class CafeMemberController {

    private final CafeMemberService cafeMemberService;

    @PostMapping("/members/{memberId}")
    public ResponseEntity<CafeMemberResponse> addCafeMember(@RequestBody @Valid CafeMemberRequest cafeMemberRequest, @PathVariable Long memberId,@PathVariable Long cafeId) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.addCafeMember(cafeMemberRequest, memberId,cafeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cafeMemberResponse);
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<CafeMemberResponse> getCafeMember(@PathVariable Long memberId,@PathVariable Long cafeId) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.getCafeMember(memberId,cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeMemberResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CafeMemberResponse>> getAllCafeMember(@PathVariable Long cafeId) {
        List<CafeMemberResponse> cafeMemberResponseList = cafeMemberService.getAllCafeMember(cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeMemberResponseList);
    }

    @PatchMapping("/members/{memberId}")
    public ResponseEntity<CafeMemberResponse> updateCafeMember(@RequestBody @Valid CafeMemberRequest cafeMemberRequest, @PathVariable Long memberId,@PathVariable Long cafeId) {
        CafeMemberResponse cafeMemberResponse = cafeMemberService.updateCafeMember(cafeMemberRequest, memberId, cafeId);
        return ResponseEntity.status(HttpStatus.OK).body(cafeMemberResponse);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteCafeMember(@PathVariable Long memberId,@PathVariable Long cafeId) {
        cafeMemberService.deleteCafeMember(memberId,cafeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
