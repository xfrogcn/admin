package com.xfrog.framework.exception;

import com.xfrog.framework.exception.response.ExceptionResponse;

/**
 * 业务异常
 */
public class BusinessException extends BaseException {
    private static final Integer DEFAULT_CODE = ExceptionCode.FAILED_PRECONDITION.getValue();

    private Integer code;

    private String details;

    public BusinessException(String message) {
        super(message);
        this.code = DEFAULT_CODE;
        this.setErrorCode(String.valueOf(this.code));
    }

    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
        this.code = DEFAULT_CODE;
    }

    public BusinessException(String errorCode, String message, String details) {
        super(errorCode, message);
        this.code = DEFAULT_CODE;
        this.details = details;
    }

    public BusinessException(String errorCode, String message, String details, Throwable cause) {
        super(errorCode, message, cause);
        this.code = DEFAULT_CODE;
        this.details = details;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ExceptionResponse toResponse() {
        return new ExceptionResponse(this.getErrorCode(), this.getCode(), this.getMessage(), this.getDetails());
    }
}
