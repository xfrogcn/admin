package com.xfrog.platform.domain.base.repository;

import com.xfrog.framework.repository.DomainRepository;
import com.xfrog.platform.domain.base.aggregate.UserParameter;

import java.util.List;

public interface UserParameterDomainRepository extends DomainRepository<UserParameter>{
    List<UserParameter> findByUserIdAndApplicationAndNames(Long userId, String application, List<String> parameterNames);
}
