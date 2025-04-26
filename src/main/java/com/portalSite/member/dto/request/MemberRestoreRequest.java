package com.portalSite.member.dto.request;

public record MemberRestoreRequest(
        String email,
        String password,
        String name,
        String phoneNumber
) {
}
