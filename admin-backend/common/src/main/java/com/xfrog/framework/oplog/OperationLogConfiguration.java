package com.xfrog.framework.oplog;


import com.xfrog.framework.oplog.aop.BeanFactoryOperationLogAdvisor;
import com.xfrog.framework.oplog.aop.OperationLogInterceptor;
import org.springframework.context.annotation.Bean;

public class OperationLogConfiguration {

    @Bean
    public BeanFactoryOperationLogAdvisor beanFactoryOperationLogAdvisor(OperationLogInterceptor interceptor) {
        BeanFactoryOperationLogAdvisor advice = new BeanFactoryOperationLogAdvisor();
        advice.setAdvice(interceptor);
        return advice;
    }

    @Bean
    public OperationLogInterceptor operationLogInterceptor(OperatorIdProvider operatorIdProvider) {
        return new OperationLogInterceptor(operatorIdProvider);
    }

    @Bean
    public OperatorIdProvider operationIdProvider() {
        return new DefaultOperatorIdProvider();
    }
}
