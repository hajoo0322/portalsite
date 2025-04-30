package com.portalSite.chatbot.entity;

import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;

public enum Feedback {
    GOOD, BAD, UNKNOWN;

    public static Feedback fromString(String value) {
        return switch (value.toUpperCase()) {
            case "GOOD" -> GOOD;
            case "BAD" -> BAD;
            default -> throw new CustomException(ErrorCode.BAD_REQUEST);
        };
    }
}
