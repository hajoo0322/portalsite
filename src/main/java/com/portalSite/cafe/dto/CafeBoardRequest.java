package com.portalSite.cafe.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CafeBoardRequest(
        @NotNull
        @Size(max = 30, message = "카페 게시판 이름은 30자를 초과할수없습니다.")
        String boardName
        ) {
}
