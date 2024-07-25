package com.xfrog.framework.oplog.aop;

import org.slf4j.MDC;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

class OperationLogEvaluationContext extends MethodBasedEvaluationContext {
    private final Set<String> unavailableVariables = new HashSet<>(1);


    OperationLogEvaluationContext(Object rootObject, Method method, Object[] arguments,
                           ParameterNameDiscoverer parameterNameDiscoverer) {

        super(rootObject, method, arguments, parameterNameDiscoverer);
    }

    public void addUnavailableVariable(String name) {
        this.unavailableVariables.add(name);
    }
    public void removeUnavailableVariable(String name) {
        this.unavailableVariables.remove(name);
    }



    @Override
    @Nullable
    public Object lookupVariable(String name) {
        if (this.unavailableVariables.contains(name)) {
            throw new VariableNotAvailableException(name);
        }
        // 先从MDC获取变量值，如果不存在则从上下文中获取
        String mdcValue =  MDC.get(name);
        if (mdcValue != null) {
            return mdcValue;
        }

        Object value =  super.lookupVariable(name);

        return value;
    }
}
