package com.xfrog.framework.exception.response;

/**
 * 带扩展数据的异常应答
 * @param <T> 附加数据
 */
public class ExceptionResponseExtend<T> extends ExceptionResponse {

    private T data;

    public ExceptionResponseExtend(String errorCode, Integer code, String message, String details, T data) {
        super(errorCode, code, message, details);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    protected void setData(T data) {
        this.data = data;
    }
}
