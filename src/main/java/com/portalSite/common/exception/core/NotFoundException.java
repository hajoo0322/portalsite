package com.portalSite.common.exception.core;

import com.portalSite.common.exception.custom.ErrorCode;

public class NotFoundException extends BaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(),errorCode.getStatus());
    }
}
