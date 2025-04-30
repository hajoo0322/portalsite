package com.portalSite.member.controller;

import com.portalSite.member.dto.request.*;
import com.portalSite.member.dto.response.MemberGetForAdminResponse;
import com.portalSite.member.dto.response.MemberGetForUserResponse;
import com.portalSite.member.dto.response.MemberResponse;
import com.portalSite.member.entity.MemberRole;
import com.portalSite.member.service.MemberService;
import com.portalSite.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse response = memberService.getMyInfo(authUser.memberId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{memberId}/user")
    public ResponseEntity<MemberGetForUserResponse> getMemberForUser(
            @PathVariable Long memberId
    ) {
        MemberGetForUserResponse response = memberService.getMemberForUser(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{memberId}/admin")
    @Secured(MemberRole.Authority.ADMIN)
    public ResponseEntity<MemberGetForAdminResponse> getMemberForAdmin(
            @PathVariable Long memberId
    ) {
        MemberGetForAdminResponse response = memberService.getMemberForAdmin(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @Secured(MemberRole.Authority.ADMIN)
    public ResponseEntity<List<MemberResponse>> getMembers(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<MemberResponse> members = memberService.getMembers(authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(members);
    }

    @PatchMapping("/{memberId}/update")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long memberId,
            @RequestBody @Valid MemberUpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse response = memberService.updateMember(memberId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{memberId}/change")
    public ResponseEntity<MemberResponse> changePassword(
            @PathVariable Long memberId,
            @RequestBody @Valid MemberChangePasswordRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse response = memberService.changePassword(memberId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{memberId}/role")
    @Secured(MemberRole.Authority.ADMIN)
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
            @RequestBody @Valid MemberDeleteRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        memberService.softDeleteMember(memberId, authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{memberId}/request")
    public ResponseEntity<Void> requestRestore(
            @PathVariable Long memberId,
            @RequestBody @Valid MemberRestoreRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        memberService.requestRestore(memberId, request, authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/restore")
    @Secured(MemberRole.Authority.ADMIN)
    public ResponseEntity<MemberResponse> restoreMember(
            @RequestBody MemberRestoreRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse response = memberService.restoreMember(request, authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
