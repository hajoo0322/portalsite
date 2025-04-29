package com.portalSite.common.exception.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //auth
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    //blog
    BLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 블로그입니다."),

    //cafe
    CAFE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카페입니다."),

    //like

    //news


    ;
    private final HttpStatus status;
    private final String message;
}
