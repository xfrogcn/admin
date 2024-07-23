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

    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    private final Map<ExpressionKey, Expression> unlessCache = new ConcurrentHashMap<>(64);

    private final OperationLogEvaluationContextFactory evaluationContextFactory;

    OperationLogExpressionEvaluator(OperationLogEvaluationContextFactory evaluationContextFactory) {
        super();
        this.evaluationContextFactory = evaluationContextFactory;
        this.evaluationContextFactory.setParameterNameDiscoverer(this::getParameterNameDiscoverer);
    }

    /**
     * Create an {@link EvaluationContext}.
     * @param method the method
     * @param args the method arguments
     * @param target the target object
     * @param targetClass the target class
     * @param result the return value (can be {@code null}) or
     * {@link #NO_RESULT} if there is no return at this time
     * @return the evaluation context
     */
    public EvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Class<?> targetClass, Method targetMethod,
                                                     @Nullable Object result) {

        OperationLogExpressionRootObject rootObject = new OperationLogExpressionRootObject(
                method, args, target, targetClass);
        OperationLogEvaluationContext evaluationContext = this.evaluationContextFactory
                .forOperation(rootObject, targetMethod, args);
        if (result == RESULT_UNAVAILABLE) {
            evaluationContext.addUnavailableVariable(RESULT_VARIABLE);
        }
        else if (result != NO_RESULT) {
            evaluationContext.setVariable(RESULT_VARIABLE, result);
        }
        return evaluationContext;
    }

    @Nullable
    public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
    }

    public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression).getValue(
                evalContext, Boolean.class)));
    }

    public boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(getExpression(this.unlessCache, methodKey, unlessExpression).getValue(
                evalContext, Boolean.class)));
    }

    /**
     * Clear all caches.
     */
    void clear() {
        this.keyCache.clear();
        this.conditionCache.clear();
        this.unlessCache.clear();
    }
}
