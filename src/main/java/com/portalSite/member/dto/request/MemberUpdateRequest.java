package com.portalSite.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberUpdateRequest(
        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^010\\d{8}$", message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다.")
        String phoneNumber,

        @NotBlank(message = "별명을 입력해주세요.")
        @Size(min = 4, max = 10, message = "별명은 4에서 10글자 사이의 단어여야 합니다.")
        String nickname
) {
}
