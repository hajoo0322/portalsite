package com.portalSite.cafe.dto;

import com.portalSite.cafe.entity.CafeBoard;

public record CafeBoardResponse(
        Long id,
        Long cafeId,
        String boardName
) {

    public static CafeBoardResponse from(CafeBoard cafeBoard) {
        return new CafeBoardResponse(cafeBoard.getId(), cafeBoard.getCafe().getId(), cafeBoard.getBoardName());
    }
}
