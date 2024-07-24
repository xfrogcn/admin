package com.xfrog.framework.oplog.aop;

import com.xfrog.framework.common.JsonHelper;

import java.lang.reflect.Method;

class OperationLogExpressionRootObject {

    private final Method method;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;


    OperationLogExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass) {

        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.args = args;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getMethodName() {
        return this.method.getName();
    }

    public Object[] getArgs() {
        return this.args;
    }

    public Object getTarget() {
        return this.target;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public String json(Object obj) {
         return JsonHelper.serialize(obj);
    }

    public String format(String format, Object... args) {
        return String.format(format, args);
    }
}
