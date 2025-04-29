package com.portalSite.common.exception.core;

import com.portalSite.common.exception.custom.ErrorCode;

public class DuplicateNameException extends BaseException {
    public DuplicateNameException(ErrorCode errorCode) {
        super(errorCode.getMessage(),errorCode.getStatus());
    }
}
