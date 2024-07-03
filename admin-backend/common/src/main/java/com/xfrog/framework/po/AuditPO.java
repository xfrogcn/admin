package com.xfrog.framework.po;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class AuditPO extends IdPO implements Serializable {
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
    @TableField(fill = FieldFill.UPDATE)
    private Long deletedBy;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime deletedTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
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
