package com.xfrog.platform.infrastructure.base.repository;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xfrog.platform.domain.base.aggregate.UserParameter;
import com.xfrog.platform.domain.base.repository.UserParameterDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.UserParameterPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.UserParameterPO;
import com.xfrog.platform.infrastructure.base.mapper.UserParameterMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserParameterDomainRepositoryImpl extends BaseDomainRepository<UserParameter, UserParameterPO, UserParameterMapper>
        implements UserParameterDomainRepository {

    public UserParameterDomainRepositoryImpl(UserParameterMapper userParameterMapper) {
        mapper = userParameterMapper;
        converter = UserParameterPOConverter.INSTANCE;
    }

    @Override
    public List<UserParameter> findByUserIdAndApplicationAndNames(Long userId, String application, List<String> parameterNames) {
        if (CollectionUtils.isEmpty(parameterNames)) {
            return new ArrayList<>();
        }
        List<UserParameterPO> userParameterPOS = mapper.selectList(new LambdaUpdateWrapper<UserParameterPO>()
                .eq(UserParameterPO::getUserId, userId)
                .eq(UserParameterPO::getApplication, application)
                .in(UserParameterPO::getParameterName, parameterNames)
                .eq(UserParameterPO::getDeleted, false)
        );

        return converter.toDomainList(userParameterPOS);
    }
}
