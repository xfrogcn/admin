package com.xfrog.platform.application.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2SessionDTO {
    private String sessionId;
    private Long userId;
    private Date lastRequest;
    private UserDetailsDTO principalInfo;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
