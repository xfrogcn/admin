package com.xfrog.framework.oplog.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

public class BeanFactoryOperationLogAdvisor  extends AbstractBeanFactoryPointcutAdvisor {

    private final OperationLogPointcut pointcut = new OperationLogPointcut();

    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
