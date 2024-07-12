package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.repository.LangCorpusRepository;
import com.xfrog.platform.infrastructure.base.converter.LangCorpusPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangCorpusPO;
import com.xfrog.platform.infrastructure.base.mapper.LangCorpusMapper;
import com.xfrog.platform.infrastructure.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LangCorpusRepositoryImpl implements LangCorpusRepository {

    private final LangCorpusMapper langCorpusMapper;

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "lc.created_time",
                    "corpusType", "lc.corpus_type",
                    "corpusGroup", "lc.corpus_group",
                    "application", "lc.application",
                    "corpusCode", "lc.corpus_code"));

    @Override
    public PageDTO<LangCorpusDTO> queryAll(QueryLangCorpusRequestDTO queryDTO) {
        Page<LangCorpusDTO> page = PageUtils.page(queryDTO, ORDER_FIELD_MAP);
        List<LangCorpusDTO> langCorpusDTOS = langCorpusMapper.queryAll(queryDTO, page);
        return PageUtils.result(page, langCorpusDTOS);
    }

    @Override
    public LangCorpusDTO queryById(Long id) {
        LangCorpusPO langCorpusPO = langCorpusMapper.selectById(id);
        return LangCorpusPOConverter.INSTANCE.toDTO(langCorpusPO);
    }
}
