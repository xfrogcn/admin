package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.LangCorpusPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangCorpusPO;
import com.xfrog.platform.infrastructure.base.mapper.LangCorpusMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LangCorpusDomainRepositoryImpl extends BaseDomainRepository<LangCorpus, LangCorpusPO, LangCorpusMapper>
        implements LangCorpusDomainRepository {
    public LangCorpusDomainRepositoryImpl(LangCorpusMapper langCorpusMapper) {
        mapper = langCorpusMapper;
        converter =LangCorpusPOConverter.INSTANCE;
    }

    @Override
    public List<LangCorpus> findAllByApplication(String application) {
        LambdaQueryWrapper<LangCorpusPO> queryWrapper = new LambdaQueryWrapper<LangCorpusPO>()
                .eq(LangCorpusPO::getApplication, application)
                .eq(LangCorpusPO::getDeleted, false)
                .eq(LangCorpusPO::getEnabled, true);
        return converter.toDomainList(mapper.selectList(queryWrapper));
    }

    @Override
    public List<LangCorpus> findByApplicationAndCodes(String application, List<String> codes) {
        LambdaQueryWrapper<LangCorpusPO> queryWrapper = new LambdaQueryWrapper<LangCorpusPO>()
                .eq(LangCorpusPO::getDeleted, false)
                .eq(LangCorpusPO::getApplication, application)
                .in(LangCorpusPO::getCorpusCode, codes);
        return converter.toDomainList(mapper.selectList(queryWrapper));
    }
}