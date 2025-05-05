package com.portalSite.common.exception.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //auth
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "해당 이메일로 가입된 정보가 있습니다."),
    ID_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "ID가 중복됩니다."),
    LOGIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 올바르지 않습니다."),

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
    NEWS_CATEGORY_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다."),
    PARENT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상위 카테고리입니다."),
    NEWS_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    NEWS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 뉴스입니다."),
    AUTHOR_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 작성자입니다."),
    NO_UPDATE_PERMISSION(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    NO_DELETE_PERMISSION(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다."),

    //member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 멤버입니다."),


    //chatbot
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 CHAT ROOM 입니다."),
    CHAT_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 CHAT LOG 입니다."),
    CHAT_FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 FAQ 입니다."),
    CHATROOM_IS_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "이미 종료된 문의내역입니다."),
    ALREADY_FEEDBACK(HttpStatus.BAD_REQUEST, "이미 관련 피드백이 존재합니다."),
    ;
    private final HttpStatus status;
    private final String message;
}
