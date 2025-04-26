package com.portalSite.cafe.dto;

import com.portalSite.cafe.entity.CafePost;

public record CafePostResponse(
        Long id,
        Long cafeId,
        Long cafeBoardId,
        String cafeMemberName,
        String title,
        String description
) {
    public static CafePostResponse from(CafePost cafePost) {
        return new CafePostResponse(
                cafePost.getId(),
                cafePost.getCafe().getId(),
                cafePost.getCafeBoard().getId(),
                cafePost.getCafeMember().getNickname(),
                cafePost.getTitle(),
                cafePost.getDescription());
    }
}
