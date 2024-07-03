package com.xfrog.framework.exception.business;

import com.xfrog.framework.exception.BusinessException;
import com.xfrog.framework.exception.ExceptionCode;

public class AlreadyExistsException extends BusinessException {
    public AlreadyExistsException(String errorCode) {
        super(errorCode);
        this.setCode(ExceptionCode.ALREADY_EXISTS.getValue());
    }

    public AlreadyExistsException(String errorCode, String message) {
        super(errorCode, message);
        this.setCode(ExceptionCode.ALREADY_EXISTS.getValue());
    }

    public AlreadyExistsException(String errorCode, String message, String details) {
        super(errorCode, message, details);
        this.setCode(ExceptionCode.ALREADY_EXISTS.getValue());
    }
}
