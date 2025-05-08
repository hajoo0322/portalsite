package com.portalSite.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewsCreateRequest(
    @NotNull(message = "하위 카테고리를 선택해주세요.")
    Long categoryId,

    @NotBlank(message = "기사 제목은 필수입니다.")
    String newsTitle,

    @NotBlank(message = "기사 내용 입력은 필수입니다.")
    String description) {

}
