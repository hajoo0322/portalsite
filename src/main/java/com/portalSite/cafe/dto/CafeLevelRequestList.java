package com.portalSite.cafe.dto;

import com.portalSite.common.valid.NoDuplicateGradeOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@NoDuplicateGradeOrder
public record CafeLevelRequestList(
        @NotNull(message = "등급이 존재하지 않습니다.")
        @Size(min = 1,max = 6,message = "등급은 1~6개만 설정가능합니다.")
        @Valid
        List<CafeLevelRequest> cafeLevelRequestList
) {
}
