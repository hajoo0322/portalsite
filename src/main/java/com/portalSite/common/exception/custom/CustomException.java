package com.portalSite.common.exception.custom;

import com.portalSite.common.exception.core.BaseException;

public class CustomException extends BaseException {
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }
}
