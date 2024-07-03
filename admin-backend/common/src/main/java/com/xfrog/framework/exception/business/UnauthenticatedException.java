package com.xfrog.framework.exception.business;

import com.xfrog.framework.exception.ExceptionCode;
import com.xfrog.framework.exception.BusinessException;

public class UnauthenticatedException extends BusinessException {
    public UnauthenticatedException(String errorCode) {
        super(errorCode);
        this.setCode(ExceptionCode.UNAUTHENTICATED.getValue());
    }

    public UnauthenticatedException(String errorCode, String message) {
        super(errorCode, message);
        this.setCode(ExceptionCode.UNAUTHENTICATED.getValue());
    }

    public UnauthenticatedException(String errorCode, String message, String details) {
        super(errorCode, message, details);
        this.setCode(ExceptionCode.UNAUTHENTICATED.getValue());
    }
}
