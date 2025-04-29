package com.portalSite.common.exception.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //auth
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    //blog
    BLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 블로그입니다."),

    //cafe
    CAFE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카페입니다."),

    //like

    //news


    //chatbot
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 ROOM,.,, 작명실패"),
    CHATROOM_IS_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "이미 종료된 문의입니다... 작명 구림 수정예정")
    ;
    private final HttpStatus status;
    private final String message;
}
