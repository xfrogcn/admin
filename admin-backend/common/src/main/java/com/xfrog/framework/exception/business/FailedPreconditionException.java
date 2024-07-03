package com.xfrog.framework.exception.business;

import com.xfrog.framework.exception.ExceptionCode;
import com.xfrog.framework.exception.BusinessException;

public class FailedPreconditionException extends BusinessException {
    public FailedPreconditionException(String errorCode) {
        super(errorCode);
        this.setCode(ExceptionCode.FAILED_PRECONDITION.getValue());
    }

    public FailedPreconditionException(String errorCode, String message) {
        super(errorCode, message);
        this.setCode(ExceptionCode.FAILED_PRECONDITION.getValue());
    }

    public FailedPreconditionException(String errorCode, String message, String details) {
        super(errorCode, message, details);
        this.setCode(ExceptionCode.FAILED_PRECONDITION.getValue());
    }
}
