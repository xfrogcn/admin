package com.xfrog.framework.exception.response;

public class ExceptionResponse {
    private String errorCode;
    private Integer code;
    private String message;
    private String details;

    public ExceptionResponse(String errorCode, Integer code, String message, String details) {
        this.errorCode = errorCode;
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    protected void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    protected void setCode(Integer code) {
        this.code = code;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected void setDetails(String details) {
        this.details = details;
    }
}
