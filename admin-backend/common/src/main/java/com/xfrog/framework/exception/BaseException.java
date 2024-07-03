package com.xfrog.framework.exception;

public abstract class BaseException extends RuntimeException {
    private String errorCode;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
