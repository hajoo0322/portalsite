package com.portalSite.cafe.dto;

public record CafeLevelAndCafeMemberRequest(
        CafeLevelRequestList cafeLevelRequestList,
        CafeMemberRequest cafeMemberRequest
) {
}
