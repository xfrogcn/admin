package com.xfrog.framework.oplog.aop;

import lombok.Setter;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.function.SingletonSupplier;

import java.lang.reflect.Method;
import java.util.function.Supplier;

class OperationLogEvaluationContextFactory {
    private final StandardEvaluationContext originalContext;

    @Setter
    @Nullable
    private Supplier<ParameterNameDiscoverer> parameterNameDiscoverer;

    OperationLogEvaluationContextFactory(StandardEvaluationContext originalContext) {
        this.originalContext = originalContext;
    }

    public ParameterNameDiscoverer getParameterNameDiscoverer() {
        if (this.parameterNameDiscoverer == null) {
            this.parameterNameDiscoverer = SingletonSupplier.of(new DefaultParameterNameDiscoverer());
        }
        return this.parameterNameDiscoverer.get();
    }

    public OperationLogEvaluationContext forOperation(OperationLogExpressionRootObject rootObject,
                                               Method targetMethod, Object[] args) {

        OperationLogEvaluationContext evaluationContext = new OperationLogEvaluationContext(
                rootObject, targetMethod, args, getParameterNameDiscoverer());
        this.originalContext.applyDelegatesTo(evaluationContext);
        return evaluationContext;
    }
}
