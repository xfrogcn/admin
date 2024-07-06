package com.xfrog.platform.infrastructure.base.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.AuditPO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 本地语言
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("lang_locales")
public class LangLocalPO extends AuditPO {
    // 所属应用
    private String application;
    // 语言ID
    private Long langId;
    // 语言标准编码
    private String langCode;
    // 语料ID
    private Long langCorpusId;
    // 语料编码
    private String langCorpusCode;
    // 本地语言
    private String localValue;
    // 是否已配置
    private Boolean configured;
}
