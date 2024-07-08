package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.LangPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangPO;
import com.xfrog.platform.infrastructure.base.mapper.LangMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LangDomainRepositoryImpl extends BaseDomainRepository<Lang, LangPO, LangMapper>
        implements LangDomainRepository {
    public LangDomainRepositoryImpl(LangMapper langMapper) {
        mapper = langMapper;
        converter = LangPOConverter.INSTANCE;
    }

    @Override
    public boolean existsByCode(String application, String code) {
        LambdaQueryWrapper<LangPO> queryWrapper = new LambdaQueryWrapper<LangPO>()
                .eq(LangPO::getDeleted, false)
                .eq(LangPO::getApplication, application)
                .eq(LangPO::getCode, code);
        return mapper.exists(queryWrapper);
    }

    @Override
    public List<Lang> findByApplication(String application, Boolean enabled) {
        LambdaQueryWrapper<LangPO> queryWrapper = new LambdaQueryWrapper<LangPO>()
                .eq(LangPO::getDeleted, false)
                .eq(LangPO::getApplication, application);
        if (enabled != null) {
            queryWrapper = queryWrapper.eq(LangPO::getEnabled, enabled);
        }

        return converter.toDomainList(mapper.selectList(queryWrapper));
    }
}
