package com.xfrog.framework.oplog.aop;

import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.framework.oplog.OperationLogEvent;
import com.xfrog.framework.oplog.OperatorIdProvider;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.framework.oplog.domain.OpLog;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class )
class OperationLogInterceptorTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private OperatorIdProvider operatorIdProvider;

    @InjectMocks
    private OperationLogInterceptor interceptor;

    @BeforeEach
    public void setUp() {
        interceptor.setApplicationEventPublisher(eventPublisher);
    }

    private static class OperationLogTestTarget {
         public void noAnnotationMethod() {

         }

        @OperationLog(executeBeforeFunc = true, bizId = "#p0", bizType = "type", bizAction = "action", msg = "format('msg %s',1)", extra = "json('extra')", tag = "tag")
        public boolean annotatedMethod(Long id) {
             return false;
        }

        @OperationLog(bizId = "#p0", bizType = "type", bizAction = "action", msg = "format('msg %s',1)", extra = "json('extra')", success = "#return", tag = "tag")
        public boolean annotatedMethodWithException(Long id) {
            throw new NotFoundException("not found");
        }

        @OperationLog(bizId = "#p0", bizTypeSpel = "'type'", bizActionSpel = "'action'", msg = "format('msg %s',1)", extra = "json('extra')", success = "#return", tag = "tag")
        public boolean annotatedAfterMethod(Long id) {
             return false;
        }

        @OperationLog(condition = "#id == 2", bizId = "#p0", bizType = "type", bizAction = "action", msg = "format('msg %s',1)", extra = "json('extra')", success = "#return", tag = "tag")
        public boolean annotatedAfterMethodCondition(Long id) {
            return false;
        }
    }

    @Test
    void invoke_NoOperationLogAnnotation_ReturnsInvocationResult() throws Throwable {
        // Arrange
        OperationLogTestTarget target = new OperationLogTestTarget();
        MethodInvocation invocation = mock(MethodInvocation.class);

        when(invocation.getMethod()).thenReturn(target.getClass().getMethod("noAnnotationMethod"));

        // Act
        Object result = interceptor.invoke(invocation);

        // Assert
        verify(invocation, times(1)).proceed();
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void invoke_WithBeforeOperationLogAnnotation_PublishesEvent() throws Throwable {
        // Arrange
        OperationLogTestTarget target = new OperationLogTestTarget();
        MethodInvocation invocation = mock(MethodInvocation.class);
        when(invocation.getMethod()).thenReturn(target.getClass().getMethod("annotatedMethod", Long.class));
        when(invocation.getThis()).thenReturn(target);
        when(invocation.getArguments()).thenReturn(new Object[]{1L});
        when(operatorIdProvider.getOperatorId()).thenReturn(1L);

        // Act
        interceptor.invoke(invocation);

        // Assert
        verify(invocation, times(1)).proceed();
        ArgumentCaptor<OperationLogEvent> captor = ArgumentCaptor.forClass(OperationLogEvent.class);
        verify(eventPublisher, times(1)).publishEvent(captor.capture());
        List<OperationLogEvent> events = captor.getAllValues();

        assertThat(events).hasSize(1);
        assertThat(events.get(0).getLogs()).hasSize(1);
        OpLog opLog = events.get(0).getLogs().get(0);
        assertThat(opLog).isNotNull();
        assertThat(opLog.getOperatorId()).isEqualTo(1);
        assertThat(opLog.getBizId()).isEqualTo("1");
        assertThat(opLog.getBizType()).isEqualTo("type");
        assertThat(opLog.getBizAction()).isEqualTo("action");
        assertThat(opLog.getMessage()).isEqualTo("msg 1");
        assertThat(opLog.getExtra()).isEqualTo("\"extra\"");
        assertThat(opLog.getTag()).isEqualTo("tag");
        assertThat(opLog.getSuccess()).isNull();
    }

    @Test
    public void invoke_WithAfterOperationLogAnnotation_PublishesEvent() throws Throwable {
        // Arrange
        OperationLogTestTarget target = new OperationLogTestTarget();
        MethodInvocation invocation = mock(MethodInvocation.class);
        when(invocation.getMethod()).thenReturn(target.getClass().getMethod("annotatedAfterMethod", Long.class));
        when(invocation.getThis()).thenReturn(target);
        when(invocation.getArguments()).thenReturn(new Object[]{1L});
        when(operatorIdProvider.getOperatorId()).thenReturn(1L);

        // Act
        MDC.put("traceId", "traceId");
        interceptor.invoke(invocation);

        // Assert
        verify(invocation, times(1)).proceed();
        ArgumentCaptor<OperationLogEvent> captor = ArgumentCaptor.forClass(OperationLogEvent.class);
        verify(eventPublisher, times(1)).publishEvent(captor.capture());
        List<OperationLogEvent> events = captor.getAllValues();

        assertThat(events).hasSize(1);
        assertThat(events.get(0).getLogs()).hasSize(1);
        OpLog opLog = events.get(0).getLogs().get(0);
        assertThat(opLog).isNotNull();
        assertThat(opLog.getOperatorId()).isEqualTo(1);
        assertThat(opLog.getBizId()).isEqualTo("1");
        assertThat(opLog.getBizType()).isEqualTo("type");
        assertThat(opLog.getBizAction()).isEqualTo("action");
        assertThat(opLog.getMessage()).isEqualTo("msg 1");
        assertThat(opLog.getExtra()).isEqualTo("\"extra\"");
        assertThat(opLog.getTag()).isEqualTo("tag");
        assertThat(opLog.getRequestId()).isEqualTo("traceId");
        assertThat(opLog.getSuccess()).isFalse();
    }

    @Test
    public void invoke_WithAfterOperationLogAnnotation_Condition_PublishesEvent() throws Throwable {
        // Arrange
        OperationLogTestTarget target = new OperationLogTestTarget();
        MethodInvocation invocation = mock(MethodInvocation.class);
        when(invocation.getMethod()).thenReturn(target.getClass().getMethod("annotatedAfterMethodCondition", Long.class));
        when(invocation.getThis()).thenReturn(target);
        when(invocation.getArguments()).thenReturn(new Object[]{1L});
        when(operatorIdProvider.getOperatorId()).thenReturn(1L);

        // Act
        interceptor.invoke(invocation);

        // Assert
        verify(invocation, times(1)).proceed();
        verify(eventPublisher, never()).publishEvent(any());
    }


    @Test
    public void invoke_WithAfterOperationLogAnnotation_AndException_PublishesEvent() throws Throwable {
        // Arrange
        OperationLogTestTarget target = new OperationLogTestTarget();
        MethodInvocation invocation = mock(MethodInvocation.class);
        when(invocation.getMethod()).thenReturn(target.getClass().getMethod("annotatedMethodWithException", Long.class));
        when(invocation.getThis()).thenReturn(target);
        when(invocation.getArguments()).thenReturn(new Object[]{1L});
        when(invocation.proceed()).thenThrow(new NotFoundException("not found"));
        when(operatorIdProvider.getOperatorId()).thenReturn(1L);

        // Act
        try {
            interceptor.invoke(invocation);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertThat(ex).isInstanceOf(NotFoundException.class);
        }

        // Assert
        verify(invocation, times(1)).proceed();
        ArgumentCaptor<OperationLogEvent> captor = ArgumentCaptor.forClass(OperationLogEvent.class);
        verify(eventPublisher, times(1)).publishEvent(captor.capture());
        List<OperationLogEvent> events = captor.getAllValues();

        assertThat(events).hasSize(1);
        assertThat(events.get(0).getLogs()).hasSize(1);
        OpLog opLog = events.get(0).getLogs().get(0);
        assertThat(opLog).isNotNull();
        assertThat(opLog.getOperatorId()).isEqualTo(1);
        assertThat(opLog.getBizId()).isEqualTo("1");
        assertThat(opLog.getBizType()).isEqualTo("type");
        assertThat(opLog.getBizAction()).isEqualTo("action");
        assertThat(opLog.getMessage()).isEqualTo("msg 1");
        assertThat(opLog.getExtra()).isEqualTo("\"extra\"");
        assertThat(opLog.getTag()).isEqualTo("tag");
        assertThat(opLog.getSuccess()).isFalse();
    }

    @Test
    public void truncateString_InputLengthLessThanOrEqualToMaxLength_ReturnsSameString() {
        String input = "abc";
        int maxLength = 10;
        String result = interceptor.truncateString(input, maxLength);
        assertEquals(input, result);
    }

    @Test
    public void truncateString_InputLengthGreaterThanMaxLength_ReturnsTruncatedStringWithEllipsis() {
        String input = "abcdefghij";
        int maxLength = 5;
        String result = interceptor.truncateString(input, maxLength);
        assertEquals("ab...", result);
    }

    @Test
    public void truncateString_InputLengthGreaterThanMaxLengthWithNoSpaceForEllipsis_ReturnsEmptyString() {
        String input = "abcdefghij";
        int maxLength = 3;
        String result = interceptor.truncateString(input, maxLength);
        assertEquals("", result);
    }

    @Test
    public void truncateString_NullInput_ReturnsEmptyString() {
        String input = null;
        int maxLength = 10;
        String result = interceptor.truncateString(input, maxLength);
        assertNull(result);
    }

    @Test
    public void truncateString_EmptyInput_ReturnsEmptyString() {
        String input = "";
        int maxLength = 10;
        String result = interceptor.truncateString(input, maxLength);
        assertEquals("", result);
    }

    @Nested
    class OperationLogEvaluationContextTest {

        @Test
        void lookupVariable_ShouldThrowException_WhenVariableIsNotAvailable() throws NoSuchMethodException {
            OperationLogEvaluationContext context = createEvaluationContext();
            context.addUnavailableVariable("unavailable_var");

            assertThrows(VariableNotAvailableException.class, () -> context.lookupVariable("unavailable_var"));
        }

        @Test
        void lookupVariable_ShouldReturnMDCParameter() throws NoSuchMethodException {
            OperationLogEvaluationContext context = createEvaluationContext();
            MDC.put("mdc_test", "mdc_test");

           String result = (String) context.lookupVariable("mdc_test");
           assertThat(result).isEqualTo("mdc_test");
        }

    }

    @Nested
    class OperationLogExpressionEvaluatorTest {
        @Test
        void clear_shouldSuccess() throws NoSuchMethodException {
            final OperationLogExpressionEvaluator evaluator = createEvaluator();
            OperationLogTestTarget target = new OperationLogTestTarget();
            Method method = target.getClass().getMethod("annotatedAfterMethod", Long.class);

            OperationLogEvaluationContext context = evaluator.createEvaluationContext(method, new Object[]{1L}, target, target.getClass(), method, null, 1L);
            evaluator.bizId("'bizId'",
                    new AnnotatedElementKey(method, target.getClass()),
                    context);
            evaluator.updateResult(context, OperationLogExpressionEvaluator.NO_RESULT);

            evaluator.clear();

            assertThat(evaluator.bizIdCache).isEmpty();
        }
    }

    private OperationLogEvaluationContext createEvaluationContext() throws NoSuchMethodException {
        final OperationLogExpressionEvaluator evaluator = createEvaluator();
        OperationLogTestTarget target = new OperationLogTestTarget();
        Method method = target.getClass().getMethod("annotatedAfterMethod", Long.class);

        return evaluator.createEvaluationContext(method, new Object[]{1L}, target, target.getClass(), method, null, 1L);
    }

    private OperationLogExpressionEvaluator createEvaluator() {
        final StandardEvaluationContext originalEvaluationContext = new StandardEvaluationContext();
        return new OperationLogExpressionEvaluator(
                new OperationLogEvaluationContextFactory(originalEvaluationContext));
    }

}