package com.xfrog.platform.api.base;

import com.xfrog.framework.common.DateTimeRange;
import com.xfrog.framework.common.SortOrder;
import com.xfrog.framework.dto.SortItem;
import com.xfrog.platform.api.base.fixtures.BaseApiFixtures;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OpLogApiTest extends BaseBaseApiTest {
    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_OP_LOGS})
    void listOpLogs_should_success() {
        QueryOpLogRequestDTO requestDTO = QueryOpLogRequestDTO.builder()
                .pageNum(1)
                .pageSize(10)
                .bizActions(List.of("update"))
                .bizTypes(List.of("dic"))
                .tags(List.of("operation"))
                .timeRange(DateTimeRange.of(LocalDateTime.now(), null))
                .keyword("test")
                .sortItems(List.of(
                        new SortItem("createdTime", SortOrder.ASC),
                        new SortItem("bizType", SortOrder.ASC),
                        new SortItem("bizAction", SortOrder.ASC),
                        new SortItem("bizCode", SortOrder.ASC),
                        new SortItem("tag", SortOrder.ASC),
                        new SortItem("operatorUserName", SortOrder.ASC),
                        new SortItem("operatorName", SortOrder.ASC)
                ))
                .build();

        request(post(url("/api/oplogs/list"), requestDTO))
                .andExpect(status().isOk());
    }
}
