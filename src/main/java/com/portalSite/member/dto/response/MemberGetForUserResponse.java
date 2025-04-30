package com.portalSite.member.dto.response;

import com.portalSite.member.entity.Member;

public record MemberGetForUserResponse(
        Long id,
        String email,
        String nickname
) {

    public static MemberGetForUserResponse from(Member member) {
        return new MemberGetForUserResponse(member.getId(), member.getEmail(), member.getNickname());
    }
}
