package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LangLocal extends AuditEntity {
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
