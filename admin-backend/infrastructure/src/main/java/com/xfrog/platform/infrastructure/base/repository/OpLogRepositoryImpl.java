package com.xfrog.platform.infrastructure.base.repository;

import com.xfrog.platform.application.base.dto.OpLogDTO;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;
import com.xfrog.platform.application.base.repository.OpLogRepository;
import com.xfrog.platform.infrastructure.base.converter.OpLogPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.OpLogPO;
import com.xfrog.platform.infrastructure.base.mapper.OpLogMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BasePageableApplicationRepository;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class OpLogRepositoryImpl extends BasePageableApplicationRepository<OpLogDTO, OpLogPO, OpLogMapper, QueryOpLogRequestDTO>
        implements OpLogRepository {
    public OpLogRepositoryImpl(OpLogMapper mapper) {
        super(mapper, OpLogPOConverter.INSTANCE);
    }

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "log.created_time",
                    "bizType", "log.biz_type",
                    "bizAction", "log.biz_action",
                    "tag", "log.tag",
                    "operatorUserName", "operatorUserName",
                    "operatorName", "operatorName",
                    "bizCode", "log.biz_code"));

    @Override
    protected Map<String, String> orderedFieldMap() {
        return ORDER_FIELD_MAP;
    }
}
