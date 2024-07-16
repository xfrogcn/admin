package com.xfrog.platform.infrastructure.base.repository;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xfrog.platform.application.base.repository.UserParameterRepository;
import com.xfrog.platform.infrastructure.base.dataobject.UserParameterPO;
import com.xfrog.platform.infrastructure.base.mapper.UserParameterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserParameterRepositoryImpl implements UserParameterRepository {

    private final UserParameterMapper mapper;

    @Override
    public Map<String, String> queryUserParameters(Long userId, String application) {

        List<UserParameterPO> parameterPOS = mapper.selectList(new LambdaUpdateWrapper<UserParameterPO>()
                .eq(UserParameterPO::getDeleted, false)
                .eq(UserParameterPO::getUserId, userId)
                .eq(UserParameterPO::getApplication, application));

        if (CollectionUtils.isEmpty(parameterPOS)) {
            return new HashMap<>();
        }

        return parameterPOS.stream()
                .filter(it -> it.getParameterValue() != null)
                .collect(Collectors.toMap(UserParameterPO::getParameterName, UserParameterPO::getParameterValue, (a, b) -> a));
    }
}
