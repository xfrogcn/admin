package com.xfrog.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuditDTO extends IdDTO implements Serializable {
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;
    private LocalDateTime deletedTime;
    private Boolean deleted;
}
