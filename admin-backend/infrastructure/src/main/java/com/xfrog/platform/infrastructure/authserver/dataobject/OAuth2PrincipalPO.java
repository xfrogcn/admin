package com.xfrog.platform.infrastructure.authserver.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.IdPO;
import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import com.xfrog.platform.infrastructure.authserver.typehandler.UserDetailDTOTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("oauth2_principals")
public class OAuth2PrincipalPO extends IdPO {
    private Long userId;
    @TableField(typeHandler = UserDetailDTOTypeHandler.class)
    private UserDetailsDTO principalInfo;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
