package com.portalSite.comment.dto.request;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;

public record CommentAlarmRequest(
        Long cafeMemberId,
        Long cafePostId
) {
}
