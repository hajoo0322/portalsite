package com.portalSite.cafe.dto;

import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafeRanking;

public record CafeResponse(
        Long id,
        String cafeName,
        String description,
        CafeRanking cafeRanking
) {
    public static CafeResponse from(Cafe cafe) {
        return new CafeResponse(
                cafe.getId(),
                cafe.getCafeName(),
                cafe.getDescription(),
                cafe.getCafeRanking()
        );
    }
}
