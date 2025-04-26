package com.portalSite.cafe.dto;

import com.portalSite.cafe.entity.CafeMember;

public record CafeMemberResponse(
        Long id,
        Long cafeId,
        String cafeGrade,
        int visitCount,
        String nickname

) {
    public static CafeMemberResponse from(CafeMember cafeMember) {
        return new CafeMemberResponse(cafeMember.getId(),cafeMember.getCafe().getId(),cafeMember.getCafeGrade(),cafeMember.getVisitCount(),cafeMember.getNickname());
    }
}
