package com.portalSite.cafe.dto;

public record CafeAndCafeMemberResponse(
        CafeResponse cafeResponse,
        CafeMemberResponse cafeMemberResponse
        ) {

    public static CafeAndCafeMemberResponse from(CafeResponse cafeResponse, CafeMemberResponse cafeMemberResponse) {
        return new CafeAndCafeMemberResponse(cafeResponse, cafeMemberResponse);
    }
}
