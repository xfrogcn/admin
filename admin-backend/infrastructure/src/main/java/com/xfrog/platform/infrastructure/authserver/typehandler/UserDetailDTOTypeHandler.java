package com.xfrog.platform.infrastructure.authserver.typehandler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

@MappedTypes({UserDetailsDTO.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
@Component
public class UserDetailDTOTypeHandler extends JacksonTypeHandler {
    public UserDetailDTOTypeHandler() {
        super(UserDetailsDTO.class);
    }
}
