package com.xfrog.platform.infrastructure.base.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.AuditPO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 语料PO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("lang_corpus")
public class LangCorpusPO extends AuditPO {
    // 所属应用
    private String application;
    // 语料类别
    private String corpusType;
    // 语料分组
    private String corpusGroup;
    // 语料编码
    private String corpusCode;
    // 语料说明
    private String memo;
    // 是否启用
    private Boolean enabled;
}
