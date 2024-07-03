package com.xfrog.framework.exception;

/**
 * 异常代码
 */
public enum ExceptionCode {
    /**
     * 没有错误
     */
    OK(200),
    /**
     * 客户端指定了无效的参数
     */
    INVALID_ARGUMENT(400),
    /**
     * 前置条件不符
     */
    FAILED_PRECONDITION(400),
    /**
     * 无效范围
     */
    OUT_OF_RANGE(400),
    /**
     * 未认证
     */
    UNAUTHENTICATED(401),
    /**
     * 未授权
     */
    PERMISSION_DENIED(403),
    /**
     * 未找到
     */
    NOT_FOUND(404),
    /**
     * 并发冲突
     */
    ABORTED(409),
    /**
     * 资源已存在
     */
    ALREADY_EXISTS(409),
    /**
     * 未知错误
     */
    UNKNOWN(500),
    /**
     * 内部异常
     */
    INTERNAL(500);

    private Integer value;
    ExceptionCode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
