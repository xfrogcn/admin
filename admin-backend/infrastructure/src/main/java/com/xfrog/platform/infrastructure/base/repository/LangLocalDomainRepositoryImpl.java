package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.LangLocalPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangLocalPO;
import com.xfrog.platform.infrastructure.base.mapper.LangLocalMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class LangLocalDomainRepositoryImpl extends BaseDomainRepository<LangLocal, LangLocalPO, LangLocalMapper>
        implements LangLocalDomainRepository {
    public LangLocalDomainRepositoryImpl(LangLocalMapper langLocalMapper) {
        mapper = langLocalMapper;
        converter = LangLocalPOConverter.INSTANCE;
    }

    @Override
    public List<LangLocal> findAllByApplicationAndLangId(String application, List<Long> langIds) {
        if (CollectionUtils.isEmpty(langIds)) {
            return new LinkedList<>();
        }
        LambdaQueryWrapper<LangLocalPO> queryWrapper = new LambdaQueryWrapper<LangLocalPO>()
                .eq(LangLocalPO::getDeleted, false)
                .eq(LangLocalPO::getApplication, application)
                .in(LangLocalPO::getLangId, langIds);
        return converter.toDomainList(mapper.selectList(queryWrapper));
    }

    @Override
    public List<LangLocal> findAllByApplicationAndCorpusIds(String application, List<Long> corpusIds) {
        if (CollectionUtils.isEmpty(corpusIds)) {
            return new LinkedList<>();
        }
        LambdaQueryWrapper<LangLocalPO> queryWrapper = new LambdaQueryWrapper<LangLocalPO>()
                .eq(LangLocalPO::getDeleted, false)
                .eq(LangLocalPO::getApplication, application)
                .in(LangLocalPO::getLangCorpusId, corpusIds);

        return converter.toDomainList(mapper.selectList(queryWrapper));
    }
}
