package com.portalSite.cafe.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CafeLevelRequest(
        @NotNull(message = "카페등급이름이 비어있습니다.")
        String grade,
        String description,

        @NotNull(message = "자동등업 여부를 선택해야합니다.")
        Boolean autoLevel,

        @NotNull(message = "등급 순서는 비어있을 수 없습니다.")
        @Min(value = 1, message = "등급 순서는 1보다 작을 수 없습니다.")
        @Max(value = 6, message = "등급 순서는 6을 넘을 수 없습니다.")
        Integer gradeOrder,

        @Min(value = 0, message = "방문 횟수는 0보다 작을 수 없습니다.")
        @NotNull(message = "방문 횟수는 비어있을 수 없습니다.")
        Integer visitCount,

        @Min(value = 0, message = "댓글 수는 0보다 작을 수 없습니다.")
        @NotNull(message = "댓글 수는 비어있을 수 없습니다.")
        Integer commentCount,

        @Min(value = 0, message = "게시글 수는 0보다 작을 수 없습니다.")
        @NotNull(message = "게시글 수는 비어있을 수 없습니다.")
        Integer postCount
        ) {
}
