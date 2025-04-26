package com.portalSite.cafe.dto;

import com.portalSite.cafe.entity.CafeLevel;

public record CafeLevelResponse(
        Long id,
        Long cafeId,
        String grade,
        String description,
        Boolean autoLevel
) {
    public static CafeLevelResponse from(CafeLevel cafeLevel) {
        return new CafeLevelResponse(cafeLevel.getId(), cafeLevel.getCafe().getId(), cafeLevel.getGrade(), cafeLevel.getDescription(), cafeLevel.getAutoLevel());
    }
}
