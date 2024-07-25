package com.xfrog.framework.oplog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.xfrog.framework.oplog.OpLogMDC.BIZ_CODE_KEY;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OperationLogs.class)
public @interface OperationLog {
    /**
     * 业务ID
     * SpEL表达式
     */
    String bizId();

    /**
     * 业务编码
     * 可选
     * SpEL表达式
     */
    String bizCode() default "#" + BIZ_CODE_KEY;

    /**
     * 业务类型
     */
    String bizType() default "";

    /**
     * 业务类型Spel
     */
    String bizTypeSpel() default "";

    /**
     * 业务动作
     */
    String bizAction() default "";

    /**
     * 业务动作Spel
     * @return
     */
    String bizActionSpel() default "";

    /**
     * 日志内容
     * 可选
     * SpEL表达式
     */
    String msg() default "";

    /**
     * 日志标签
     */
    String tag() default "operation";


    /**
     * 额外信息
     * 可选
     * SpEL表达式
     */
    String extra() default "";

    /**
     * 操作人ID
     * 可选
     * SpEL表达式
     */
    String operatorId() default "";

    /**
     * 执行时机
     * true: 执行方法前
     * false: 执行方法后
     */
    boolean executeBeforeFunc() default false;


    /**
     * 记录条件
     * 可选
     * SpEL表达式
     */
    String condition() default "'true'";

    /**
     * 是否成功
     * 可选
     * SpEL表达式
     */
    String success() default "";
}
