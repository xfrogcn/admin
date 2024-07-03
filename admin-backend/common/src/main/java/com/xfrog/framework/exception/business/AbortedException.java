package com.xfrog.framework.exception.business;

import com.xfrog.framework.exception.BusinessException;
import com.xfrog.framework.exception.ExceptionCode;

public class AbortedException extends BusinessException {
    public AbortedException(String errorCode) {
        super(errorCode);
        this.setCode(ExceptionCode.ABORTED.getValue());
    }

    public AbortedException(String errorCode, String message) {
        super(errorCode, message);
        this.setCode(ExceptionCode.ABORTED.getValue());
    }

    public AbortedException(String errorCode, String message, String details) {
        super(errorCode, message, details);
        this.setCode(ExceptionCode.ABORTED.getValue());
    }
}
