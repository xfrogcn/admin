package com.xfrog.platform.infrastructure.persistent.config;

import com.xfrog.platform.infrastructure.persistent.handler.IDataScopeHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface DataScopeTable {
    Class<? extends IDataScopeHandler> value();
    DataScopeColumn[] columns();
}
