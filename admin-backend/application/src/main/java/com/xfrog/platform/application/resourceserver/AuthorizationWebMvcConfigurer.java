package com.xfrog.platform.application.resourceserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class AuthorizationWebMvcConfigurer implements WebMvcConfigurer {

    private final Lazy<AuthorizationHandlerInterceptor> authorizationHandlerInterceptor;

    public AuthorizationWebMvcConfigurer(ApplicationContext applicationContext) {
        this.authorizationHandlerInterceptor = Lazy.of(() -> {
            return (AuthorizationHandlerInterceptor) applicationContext.getBean(AuthorizationHandlerInterceptor.class);
        });
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationHandlerInterceptor.get());
    }
}
