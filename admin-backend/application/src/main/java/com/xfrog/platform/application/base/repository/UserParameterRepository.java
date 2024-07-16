package com.xfrog.platform.application.base.repository;

import java.util.Map;

public interface UserParameterRepository {
    Map<String, String> queryUserParameters(Long userId, String application);
}
