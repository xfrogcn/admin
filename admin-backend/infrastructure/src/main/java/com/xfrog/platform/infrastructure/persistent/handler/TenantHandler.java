package com.xfrog.platform.infrastructure.persistent.handler;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.xfrog.platform.application.common.AnnotationScanner;
import com.xfrog.platform.application.common.RequestThreadMarkContext;
import com.xfrog.platform.infrastructure.persistent.config.TenantTable;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TenantHandler implements TenantLineHandler, InitializingBean, BeanFactoryAware {

    private final ObjectProvider<AnnotationScanner> annotationScannerObjectProvider;

    private BeanFactory beanFactory;
    private final Set<String> tenantTables = new HashSet<>();

    @Override
    public Expression getTenantId() {
        return new StringValue(RequestThreadMarkContext.currentTenantId());
    }

    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        if (RequestThreadMarkContext.threadMark().isIgnoreTenant()) {
            return true;
        }
        return !tenantTables.contains(tableName);
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
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
          if (cls.isAnnotationPresent(TenantTable.class)) {
              TableName tableName = cls.getAnnotation(TableName.class);
              tenantTables.add(tableName.value());
          }
      });

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
