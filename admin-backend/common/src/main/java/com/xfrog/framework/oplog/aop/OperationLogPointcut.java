package com.xfrog.framework.oplog.aop;

import com.xfrog.framework.oplog.annotation.OperationLog;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

final class OperationLogPointcut extends AnnotationMethodMatcher implements Pointcut {

    private ClassFilter classFilter = ClassFilter.TRUE;

    public OperationLogPointcut() {
        super(OperationLog.class);
    }

    public void setClassFilter(ClassFilter classFilter) {
        this.classFilter = classFilter;
    }

    @Override
    public ClassFilter getClassFilter() {
        return this.classFilter;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
