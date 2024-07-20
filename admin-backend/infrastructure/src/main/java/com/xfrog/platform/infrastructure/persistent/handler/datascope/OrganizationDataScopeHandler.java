package com.xfrog.platform.infrastructure.persistent.handler.datascope;

import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.framework.principal.PrincipalInfo;
import com.xfrog.platform.application.common.RequestThreadMarkContext;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import com.xfrog.platform.infrastructure.persistent.config.DataScopeColumn;
import com.xfrog.platform.infrastructure.persistent.config.DataScopeTable;
import com.xfrog.platform.infrastructure.persistent.handler.IDataScopeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrganizationDataScopeHandler implements IDataScopeHandler {
    private final ObjectProvider<OrganizationRepository> organizationRepository;
    public static final String ORGANIZATION_DATA_SCOPE_HANDLER_NAME = "organization";
    private final ConcurrentHashMap<Long, String> organizationCache = new ConcurrentHashMap<>();
    private final List<String> organizationScopeTypes = List.of(DataScopeType.ORGANIZATION.name(), DataScopeType.USER_ORGANIZATION.name());
    @Override
    public Expression getSqlSegment(DataScopeTable annotation, Table table, Expression where, String mappedStatementId) {
        PrincipalInfo principalInfo = CurrentPrincipalContext.currentPrincipal();
        if (principalInfo.isSystem()) {
            return null;
        }

        String organizationColumnName = "organization_id";
        if (annotation.columns() != null) {
            DataScopeColumn dataScopeColumn = Arrays.stream(annotation.columns()).filter(it -> ORGANIZATION_DATA_SCOPE_HANDLER_NAME.equals(it.handler()))
                    .findFirst()
                    .orElse(null);
            if (dataScopeColumn != null && StringUtils.hasText(dataScopeColumn.column())) {
                organizationColumnName = dataScopeColumn.column();
            }
        }

        List<Long> organizationIds = new LinkedList<>();
        if (!CollectionUtils.isEmpty(principalInfo.getDataPermission())) {
            organizationIds = principalInfo.getDataPermission().stream()
                    .filter(it -> organizationScopeTypes.contains(it.getScopeType()))
                    .map(it -> DataScopeType.USER_ORGANIZATION.name().equals(it.getScopeType()) ? principalInfo.getOrganizationId() : it.getScopeId())
                    .distinct()
                    .toList();
        }
        List<Long> noCachedIds = organizationIds.stream()
                .filter(id -> !organizationCache.containsKey(id))
                .toList();
        if (!CollectionUtils.isEmpty(noCachedIds)) {
            RequestThreadMarkContext.threadMark().setIgnoreDataScope(true);
            organizationRepository.getIfAvailable().queryByIds(noCachedIds).forEach(org -> organizationCache.put(org.getId(), org.getCode()));
            RequestThreadMarkContext.threadMark().setIgnoreDataScope(false);
        }

        // 去除已包含的下级组织
        List<String> organizationCodes = new LinkedList<>();
        organizationIds.stream()
            .map(organizationCache::get)
            .filter(Objects::nonNull)
            .distinct()
            .sorted((a, b) -> a.length() - b.length())
            .forEach(code -> {
                if (organizationCodes.stream().noneMatch(code::startsWith)) {
                    organizationCodes.add(code);
                }
            });
        if (organizationCodes.isEmpty()) {
            // 无权限, 永假表达式
            return new EqualsTo(new LongValue(1), new LongValue(0));
        }

        // 按组织权限过滤
        String alias = table.getAlias() != null ? table.getAlias().getName() + "." : "";
        try {
            boolean isOrganizationTable = "organizations".equalsIgnoreCase(table.getNameParts().get(0));
            Column codeColumn = new Column(isOrganizationTable ? (alias + "code") : "dp_org.code");
            Expression whereExpression = null;
            for (String code : organizationCodes) {
                LikeExpression likeExpression = new LikeExpression();
                likeExpression.withLeftExpression(codeColumn);
                likeExpression.withRightExpression(new StringValue(code + "%"));
                if (whereExpression == null) {
                    whereExpression = likeExpression;
                } else {
                    whereExpression = new OrExpression(whereExpression, likeExpression);
                }
            }

            if (isOrganizationTable) {
                // 组织本表
                return new Parenthesis(whereExpression);
            }

            PlainSelect existsSelectExpression = (PlainSelect) CCJSqlParserUtil.parse("SELECT 1 FROM organizations AS dp_org WHERE dp_org.id = " + alias + organizationColumnName);
            existsSelectExpression.setWhere(new AndExpression(existsSelectExpression.getWhere(), new Parenthesis(whereExpression)));
            return new ExistsExpression().withRightExpression(new Parenthesis(existsSelectExpression));
        } catch (Exception e) {
            log.error("解析表达式失败", e);
            throw new RuntimeException(e);
        }
    }
}
