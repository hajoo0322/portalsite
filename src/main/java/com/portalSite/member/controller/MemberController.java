package com.portalSite.member.controller;

import com.portalSite.member.dto.request.*;
import com.portalSite.member.dto.response.MemberResponse;
import com.portalSite.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(
            @RequestBody MemberRequest memberRequest
    ) {
        MemberResponse response = memberService.registerMember(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(
            @PathVariable Long memberId
    ) {
        MemberResponse response = memberService.getMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{memberId}/update/{currentId}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long memberId,
            @PathVariable Long currentId,
            @RequestBody MemberUpdateRequest request
    ) {
        MemberResponse response = memberService.updateMember(memberId, currentId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{memberId}/change/{currentId}")
    public ResponseEntity<MemberResponse> changePassword(
            @PathVariable Long memberId,
            @PathVariable Long currentId,
            @RequestBody MemberChangePasswordRequest request
    ) {
        MemberResponse response = memberService.changePassword(memberId, currentId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{memberId}/role/{currentId}")
    public ResponseEntity<MemberResponse> changeRole(
            @PathVariable Long memberId,
            @PathVariable Long currentId,
            @RequestParam(name = "role") String role
    ) {
        MemberResponse response = memberService.changeMemberRole(memberId, currentId, role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{memberId}/{currentId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long memberId,
            @PathVariable Long currentId,
            @RequestBody MemberDeleteRequest request
    ) {
        memberService.softDeleteMember(memberId, currentId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{currentId}")
    public ResponseEntity<MemberResponse> restoreMember(
            @PathVariable Long currentId,
            @RequestBody MemberRestoreRequest request
    ) {
        MemberResponse response = memberService.restoreMember(request, currentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
