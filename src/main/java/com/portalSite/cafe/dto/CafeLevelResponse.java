package com.portalSite.cafe.dto;

import com.portalSite.cafe.entity.CafeLevel;

public record CafeLevelResponse(
        Long id,
        Long cafeId,
        String grade,
        Integer gradeOrder,
        String description,
        Boolean autoLevel
) {
    public static CafeLevelResponse from(CafeLevel cafeLevel) {
        return new CafeLevelResponse(cafeLevel.getId(), cafeLevel.getCafe().getId(), cafeLevel.getGrade(),cafeLevel.getGradeOrder(), cafeLevel.getDescription(), cafeLevel.getAutoLevel());
    }
}
