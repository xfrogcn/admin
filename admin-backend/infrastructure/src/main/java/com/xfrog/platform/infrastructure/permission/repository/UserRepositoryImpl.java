package com.xfrog.platform.infrastructure.permission.repository;

import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.infrastructure.permission.converter.UserPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.UserPO;
import com.xfrog.platform.infrastructure.permission.mapper.UserMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BasePageableApplicationRepository;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl extends BasePageableApplicationRepository<UserDTO, UserPO, UserMapper, QueryUserRequestDTO>
        implements UserRepository {

    public UserRepositoryImpl(UserMapper mapper) {
        super(mapper, UserPOConverter.INSTANCE);
    }

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "u.created_time",
                    "lastLoginTime", "u.last_login_time",
                    "name", "u.name",
                    "userName", "u.user_name",
                    "organizationName", "org.name"));

    @Override
    protected Map<String, String> orderedFieldMap() {
        return ORDER_FIELD_MAP;
    }


    @Override
    public List<String> queryUserPermissionCodes(Long userId) {
        return mapper.queryUserPermissionCodes(userId);
    }
}
