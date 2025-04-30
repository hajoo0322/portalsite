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

    // duplicate
    DUPLICATE_NAME(HttpStatus.BAD_REQUEST, "중복된 이름 입니다."),
    //blog
    BLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 블로그입니다."),

    //cafe
    CAFE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카페입니다."),
    CAFE_BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지않는 게시판입니다."),
    CAFE_LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 카페 등급입니다."),
    CAFE_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 카페 멤버입니다."),
    CAFE_POST_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 카페 게시글입니다."),

    //like

    //news

    //member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 멤버입니다.")


    //chatbot
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 CHAT ROOM 입니다."),
    CHAT_FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 FAQ 입니다."),
    CHATROOM_IS_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "이미 종료된 문의내역입니다.")
    ;
    private final HttpStatus status;
    private final String message;
}
