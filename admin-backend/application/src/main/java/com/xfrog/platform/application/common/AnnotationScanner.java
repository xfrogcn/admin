package com.xfrog.platform.application.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@Component
public class AnnotationScanner implements ApplicationContextAware {
    private static final String RESOURCE_PATTERN = "/**/*.class";

    private ApplicationContext applicationContext;

    /**
     * 扫描指定包下带有特定注解的类
     * @param basePackage 包名
     * @param annotationClass 注解类型
     * @return 类集合
     */
    public Set<Class<?>> findAnnotatedClasses(String basePackage, Class<? extends Annotation> annotationClass) {
        Set<Class<?>> annotatedClasses = new HashSet<>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
                    + RESOURCE_PATTERN;
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(applicationContext);

            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    if (reader.getClassMetadata().isInterface() || reader.getClassMetadata().isAbstract()) {
                        continue;
                    }
                    Class<?> clazz = Class.forName(reader.getClassMetadata().getClassName());
                    if (clazz.isAnnotationPresent(annotationClass)) {
                        annotatedClasses.add(clazz);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // 处理异常
            e.printStackTrace();
        }
        return annotatedClasses;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
