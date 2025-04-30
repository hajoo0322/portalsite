package com.portalSite.member.dto.response;

import com.portalSite.member.entity.Member;
import com.portalSite.member.entity.MemberRole;

public record MemberGetForAdminResponse(
        Long id,
        String email,
        String loginId,
        String name,
        String phoneNumber,
        String nickname,
        MemberRole memberRole
) {
    public static MemberGetForAdminResponse from(Member member) {
        return new MemberGetForAdminResponse(
                member.getId(),
                member.getEmail(),
                member.getLoginId(),
                member.getName(),
                member.getPhoneNumber(),
                member.getNickname(),
                member.getMemberRole());
    }
}
