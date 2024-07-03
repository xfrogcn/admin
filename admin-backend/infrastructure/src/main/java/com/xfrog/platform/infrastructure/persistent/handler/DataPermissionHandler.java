package com.xfrog.platform.infrastructure.persistent.handler;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.platform.application.common.AnnotationScanner;
import com.xfrog.platform.application.common.RequestThreadMarkContext;
import com.xfrog.platform.infrastructure.persistent.config.DataScopeTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataPermissionHandler implements MultiDataPermissionHandler, InitializingBean, BeanFactoryAware {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class DataScopeHandlerInfo {
        private IDataScopeHandler handler;
        private DataScopeTable annotation;
    }

    private final ObjectProvider<AnnotationScanner> annotationScannerObjectProvider;
    private BeanFactory beanFactory;
    private final ConcurrentHashMap<String, DataScopeHandlerInfo> dataScopeHandlerMap = new ConcurrentHashMap<>();
    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        if (RequestThreadMarkContext.threadMark().isIgnoreDataScope()) {
            return null;
        }
        if (CurrentPrincipalContext.currentPrincipalOrSystem().isSystem()) {
            return null;
        }
        if (CollectionUtils.isEmpty(table.getNameParts())) {
            return null;
        }
        DataScopeHandlerInfo handlerInfo = dataScopeHandlerMap.get(table.getNameParts().get(0));
        if (handlerInfo == null) {
            return null;
        }

        return handlerInfo.handler.getSqlSegment(handlerInfo.getAnnotation(), table, where, mappedStatementId);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationScanner annotationScanner =  annotationScannerObjectProvider.getIfAvailable();
        assert annotationScanner != null;
        List<String> packages =  AutoConfigurationPackages.get(beanFactory);
        List<Class<?>> allTableClass = new LinkedList<>();
        packages.forEach((p) -> {
            allTableClass.addAll(annotationScanner.findAnnotatedClasses(p, TableName.class));
        });

        allTableClass.forEach((cls) -> {
            if (cls.isAnnotationPresent(DataScopeTable.class)) {
                DataScopeTable dataScopeTable = cls.getAnnotation(DataScopeTable.class);
                TableName tableName = cls.getAnnotation(TableName.class);
                IDataScopeHandler handler = beanFactory.getBean(dataScopeTable.value());
                dataScopeHandlerMap.put(tableName.value(), DataScopeHandlerInfo.builder()
                        .handler(handler)
                        .annotation(dataScopeTable)
                        .build());
            }
        });
    }
}
