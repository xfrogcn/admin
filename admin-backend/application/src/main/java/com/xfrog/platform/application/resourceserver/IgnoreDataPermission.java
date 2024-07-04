package com.xfrog.platform.application.resourceserver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记接口方法对应的权限编码，如果传递permissionCodes，则必须具备所有的权限才可执行，否则返回403应答
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IgnoreDataPermission {
    boolean ignoreDataScope() default true;
    boolean ignoreTenant() default true;
}
