package com.portalSite.member.dto.response;

import com.portalSite.member.entity.Member;
import com.portalSite.member.entity.MemberRole;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String email,
        String loginId,
        String password,
        String name,
        String phoneNumber,
        String nickname,
        MemberRole memberRole,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getLoginId(),
                member.getPassword(),
                member.getName(),
                member.getPhoneNumber(),
                member.getNickname(),
                member.getMemberRole(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
