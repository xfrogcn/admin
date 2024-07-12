package com.xfrog.platform.infrastructure.base.repository;

import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.repository.LangRepository;
import com.xfrog.platform.infrastructure.base.converter.LangPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangPO;
import com.xfrog.platform.infrastructure.base.mapper.LangMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BasePageableApplicationRepository;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class LangRepositoryImpl extends BasePageableApplicationRepository<LangDTO, LangPO, LangMapper, QueryLangRequestDTO>
        implements LangRepository {

    public LangRepositoryImpl(LangMapper langMapper) {
        super(langMapper, LangPOConverter.INSTANCE);
    }

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "l.created_time",
                    "application", "l.application"));

    @Override
    protected Map<String, String> orderedFieldMap() {
        return ORDER_FIELD_MAP;
    }
}
