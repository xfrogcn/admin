package com.xfrog.framework.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AuditEntity extends IdEntity implements Serializable {
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;
    private LocalDateTime deletedTime;
    private Boolean deleted;

    public void logicDelete(Long deletedBy) {
        this.deleted = true;
        this.deletedBy = deletedBy;
        this.deletedTime = LocalDateTime.now();
    }

    public void logicRestore(Long updatedBy) {
        this.deleted = false;
        this.updatedBy = updatedBy;
        this.updatedTime = LocalDateTime.now();
        this.deletedBy = null;
        this.deletedTime = null;
    }

    public void create(Long createdBy) {
        this.createdBy = createdBy;
        this.createdTime = LocalDateTime.now();
        this.updatedBy = createdBy;
        this.updatedTime = LocalDateTime.now();
        this.deleted = false;
        this.deletedBy = null;
        this.deletedTime = null;
    }

    public void update(Long updatedBy) {
        this.updatedBy = updatedBy;
        this.updatedTime = LocalDateTime.now();
    }
}
