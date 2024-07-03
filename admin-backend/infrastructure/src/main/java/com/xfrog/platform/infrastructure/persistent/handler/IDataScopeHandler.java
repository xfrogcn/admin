package com.xfrog.platform.infrastructure.persistent.handler;

import com.xfrog.platform.infrastructure.persistent.config.DataScopeTable;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

public interface IDataScopeHandler {
    Expression getSqlSegment(DataScopeTable annotation, Table table, Expression where, String mappedStatementId);
}
