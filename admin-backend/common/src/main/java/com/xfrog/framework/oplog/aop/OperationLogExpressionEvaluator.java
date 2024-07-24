package com.xfrog.framework.oplog.aop;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class OperationLogExpressionEvaluator  extends CachedExpressionEvaluator {
    public static final Object NO_RESULT = new Object();

    public static final Object RESULT_UNAVAILABLE = new Object();

    public static final String RESULT_VARIABLE = "result";

    private final Map<ExpressionKey, Expression> bizIdCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> bizCodeCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> bizExtraCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> msgCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> successCache = new ConcurrentHashMap<>(64);

    private final OperationLogEvaluationContextFactory evaluationContextFactory;

    OperationLogExpressionEvaluator(OperationLogEvaluationContextFactory evaluationContextFactory) {
        super();
        this.evaluationContextFactory = evaluationContextFactory;
        this.evaluationContextFactory.setParameterNameDiscoverer(this::getParameterNameDiscoverer);
    }

    public OperationLogEvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Class<?> targetClass, Method targetMethod,
                                                     @Nullable Object result) {

        OperationLogExpressionRootObject rootObject = new OperationLogExpressionRootObject(
                method, args, target, targetClass);
        OperationLogEvaluationContext evaluationContext = this.evaluationContextFactory
                .forOperation(rootObject, targetMethod, args);
       updateResult(evaluationContext, result);
        return evaluationContext;
    }

    public void updateResult(OperationLogEvaluationContext evaluationContext, Object result) {
        evaluationContext.removeUnavailableVariable(RESULT_VARIABLE);
        if (result == RESULT_UNAVAILABLE) {
            evaluationContext.addUnavailableVariable(RESULT_VARIABLE);
        }
        else if (result != NO_RESULT) {
            evaluationContext.setVariable(RESULT_VARIABLE, result);
        }
    }

    @Nullable
    public Object bizId(String bizIdExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.bizIdCache, methodKey, bizIdExpression).getValue(evalContext);
    }

    @Nullable
    public Object bizCode(String bizTypeExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.bizCodeCache, methodKey, bizTypeExpression).getValue(evalContext);
    }

    @Nullable
    public Object extra(String bizActionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.bizExtraCache, methodKey, bizActionExpression).getValue(evalContext);
    }

    public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression).getValue(
                evalContext, Boolean.class)));
    }

    public String msg(String msgExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.msgCache, methodKey, msgExpression).getValue(evalContext, String.class);
    }

    public boolean success(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(getExpression(this.successCache, methodKey, unlessExpression).getValue(
                evalContext, Boolean.class)));
    }

    /**
     * Clear all caches.
     */
    void clear() {
        this.bizIdCache.clear();
        this.bizCodeCache.clear();
        this.bizExtraCache.clear();
        this.msgCache.clear();
        this.conditionCache.clear();
        this.successCache.clear();
    }
}
