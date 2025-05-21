package com.portalSite.cafe.dto;

import java.util.List;

public record CafeLevelAndCafeMemberResponse(
        List<CafeLevelResponse> cafeLevelResponses,
        CafeMemberResponse cafeMemberResponse
        ) {

    public static CafeLevelAndCafeMemberResponse from(List<CafeLevelResponse> cafeResponse, CafeMemberResponse cafeMemberResponse) {
        return new CafeLevelAndCafeMemberResponse(cafeResponse, cafeMemberResponse);
    }
}
