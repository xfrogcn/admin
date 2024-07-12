package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.repository.LangCorpusRepository;
import com.xfrog.platform.infrastructure.base.converter.LangCorpusPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangCorpusPO;
import com.xfrog.platform.infrastructure.base.mapper.LangCorpusMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BasePageableApplicationRepository;
import com.xfrog.platform.infrastructure.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LangCorpusRepositoryImpl extends BasePageableApplicationRepository<LangCorpusDTO, LangCorpusPO, LangCorpusMapper, QueryLangCorpusRequestDTO>
        implements LangCorpusRepository {

    public LangCorpusRepositoryImpl(LangCorpusMapper mapper) {
        super(mapper, LangCorpusPOConverter.INSTANCE);
    }


    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "lc.created_time",
                    "corpusType", "lc.corpus_type",
                    "corpusGroup", "lc.corpus_group",
                    "application", "lc.application",
                    "corpusCode", "lc.corpus_code"));

    @Override
    protected Map<String, String> orderedFieldMap() {
        return ORDER_FIELD_MAP;
    }

}
