package com.xfrog.framework.oplog.aop;

import com.xfrog.framework.oplog.OperationLogEvent;
import com.xfrog.framework.oplog.OperatorIdProvider;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.framework.oplog.domain.OpLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class OperationLogInterceptor implements ApplicationEventPublisherAware,  MethodInterceptor, Serializable {

    private final ConversionService conversionService = new DefaultFormattingConversionService();

    private final StandardEvaluationContext originalEvaluationContext = new StandardEvaluationContext();

    private final OperationLogExpressionEvaluator evaluator = new OperationLogExpressionEvaluator(
            new OperationLogEvaluationContextFactory(this.originalEvaluationContext));

    private ApplicationEventPublisher eventPublisher;

    private final OperatorIdProvider operatorIdProvider;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();

        OperationLog[] annotations = method.getAnnotationsByType(OperationLog.class);
        if (annotations == null || annotations.length == 0) {
            return invocation.proceed();
        }

        Object target = invocation.getThis();
        Assert.state(target != null, "Target must not be null");

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        Method targetMethod = Proxy.isProxyClass(targetClass) ? method : AopUtils.getMostSpecificMethod(method, targetClass);

        Long operatorId = operatorIdProvider.getOperatorId();

        OperationLogEvaluationContext evaluationContext = evaluator.createEvaluationContext(
                method,
                invocation.getArguments(),
                target,
                targetClass,
                targetMethod,
                OperationLogExpressionEvaluator.RESULT_UNAVAILABLE);
        AnnotatedElementKey elementKey = new AnnotatedElementKey(targetMethod, targetClass);

        List<OperationLog> afterLogs = new ArrayList<>(annotations.length);
        List<OpLog> beforeLogs = new ArrayList<>(annotations.length);
        for (OperationLog opLog : annotations) {
            if (!opLog.executeBeforeFunc()) {
                afterLogs.add(opLog);
                continue;
            }
            // 记录之前的
            log(elementKey, opLog, evaluationContext, false)
                    .ifPresent(log -> beforeLogs.add(log.operatorId(operatorId).build()));
        }
        publishOperationLogs(beforeLogs);

        if (afterLogs.isEmpty()) {
            return invocation.proceed();
        }

        boolean processResult = false;
        StopWatch stopWatch = new StopWatch();
        List<OpLog> afterOpLogs = new ArrayList<>(annotations.length);
        boolean defaultSuccess = true;
        try {
            stopWatch.start();
            Object result =  invocation.proceed();
            evaluator.updateResult(evaluationContext, result);
            processResult = true;

            return result;
        } catch (Exception ex) {
            defaultSuccess = false;
            throw ex;
        } finally {
            stopWatch.stop();
            long executeTime = stopWatch.getTotalTimeMillis();
            for (OperationLog opLog : afterLogs) {
                boolean hasSuccessSpel = StringUtils.hasText(opLog.success());
                Optional<OpLog.OpLogBuilder<?, ?>> opLogOpt = log(elementKey, opLog, evaluationContext, processResult);
                if (opLogOpt.isEmpty()) {
                    continue;
                }

                OpLog.OpLogBuilder<?, ?> opLogBuilder = opLogOpt.get();
                if (!hasSuccessSpel || !defaultSuccess) {
                    opLogBuilder.success(defaultSuccess);
                }
                opLogBuilder.executeTime(executeTime);
                opLogBuilder.operatorId(operatorId);
                afterOpLogs.add(opLogBuilder.build());
            }
            publishOperationLogs(afterOpLogs);
        }
    }

    protected void publishOperationLogs(List<OpLog> logs) {
        if (CollectionUtils.isEmpty(logs)) {
            return;
        }
        if (eventPublisher == null) {
            return;
        }
        eventPublisher.publishEvent(OperationLogEvent.of(logs));
    }

    protected Optional<OpLog.OpLogBuilder<?, ?>> log(AnnotatedElementKey elementKey, OperationLog opLog, EvaluationContext evaluationContext, boolean processResult) {
        if (StringUtils.hasText(opLog.condition()) && !evaluator.condition(opLog.condition(), elementKey, evaluationContext)) {
            return Optional.empty();
        }

        OpLog.OpLogBuilder<?, ?> builder = OpLog.builder();
        if (StringUtils.hasText(opLog.bizId())) {
            builder.bizId(truncateString(conversionService.convert(evaluator.bizId(opLog.bizId(), elementKey, evaluationContext), String.class), 64));
        }
        if (StringUtils.hasText(opLog.bizCode())) {
            builder.bizCode(truncateString(conversionService.convert(evaluator.bizCode(opLog.bizCode(), elementKey, evaluationContext), String.class), 128));
        }

        if (StringUtils.hasText(opLog.bizActionSpel())) {
            builder.bizAction(conversionService.convert(evaluator.bizAction(opLog.bizActionSpel(), elementKey, evaluationContext), String.class));
        }else {
            builder.bizAction(opLog.bizAction());
        }
        if (StringUtils.hasText(opLog.bizTypeSpel())) {
            builder.bizType(conversionService.convert(evaluator.bizType(opLog.bizTypeSpel(), elementKey, evaluationContext), String.class));
        }else {
            builder.bizType(opLog.bizType());
        }

        builder.tag(opLog.tag());
        // 请求ID
        String traceId = MDC.get("traceId");
        if (StringUtils.hasText(traceId)) {
            builder.requestId(traceId);
        }

        if (StringUtils.hasText(opLog.extra())) {
            builder.extra(conversionService.convert(evaluator.extra(opLog.extra(), elementKey, evaluationContext), String.class));
        }
        if (StringUtils.hasText(opLog.msg())) {
            builder.message(evaluator.msg(opLog.msg(), elementKey, evaluationContext));
        }
        if (processResult && StringUtils.hasText(opLog.success())) {
            builder.success(evaluator.success(opLog.success(), elementKey, evaluationContext));
        }

        return Optional.of(builder);
    }

    protected String truncateString(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        } else {
            int end = maxLength - 3; // 减去省略号的长度
            if (end > 0) {
                return input.substring(0, end) + "...";
            } else {
                return "";
            }
        }
    }

}
