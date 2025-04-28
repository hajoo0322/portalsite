package com.portalSite.cafe.dto;

public record CafeAndCafeMemberRequest(
        CafeRequest cafeRequest,
        CafeMemberRequest cafeMemberRequest
) {
}
