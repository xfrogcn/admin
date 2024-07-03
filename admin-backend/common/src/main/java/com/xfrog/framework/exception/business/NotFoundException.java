package com.xfrog.framework.exception.business;

import com.xfrog.framework.exception.ExceptionCode;
import com.xfrog.framework.exception.BusinessException;

public class NotFoundException extends BusinessException {
    public NotFoundException(String errorCode) {
        super(errorCode);
        this.setCode(ExceptionCode.NOT_FOUND.getValue());
    }

    public NotFoundException(String errorCode, String message) {
        super(errorCode, message);
        this.setCode(ExceptionCode.NOT_FOUND.getValue());
    }

    public NotFoundException(String errorCode, String message, String details) {
        super(errorCode, message, details);
        this.setCode(ExceptionCode.NOT_FOUND.getValue());
    }
}
