package com.portalSite.member.controller;

import com.portalSite.member.dto.request.*;
import com.portalSite.member.dto.response.MemberResponse;
import com.portalSite.member.entity.MemberRole;
import com.portalSite.member.service.MemberService;
import com.portalSite.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(
            @PathVariable Long memberId
    ) {
        MemberResponse response = memberService.getMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{memberId}/update")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long memberId,
            @RequestBody MemberUpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse response = memberService.updateMember(memberId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{memberId}/change")
    public ResponseEntity<MemberResponse> changePassword(
            @PathVariable Long memberId,
            @RequestBody MemberChangePasswordRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse response = memberService.changePassword(memberId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{memberId}/role")
    public ResponseEntity<MemberResponse> changeRole(
            @PathVariable Long memberId,
            @RequestParam(name = "role") MemberRole role,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse response = memberService.changeMemberRole(memberId, authUser.memberId(), role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long memberId,
            @RequestBody MemberDeleteRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        memberService.softDeleteMember(memberId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/restore")
    public ResponseEntity<MemberResponse> restoreMember(
            @RequestBody MemberRestoreRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse response = memberService.restoreMember(request, authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
