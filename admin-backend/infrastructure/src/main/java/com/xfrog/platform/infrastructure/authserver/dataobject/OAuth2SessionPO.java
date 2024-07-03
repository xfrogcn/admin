package com.xfrog.platform.infrastructure.authserver.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.IdPO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("oauth2_sessions")
public class OAuth2SessionPO extends IdPO {
    private String sessionId;
    private String sessionIdHash;
    private Long userId;
    private Long principalId;
    private Date lastRequest;
    private Boolean expired;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String authorizationId;
}
