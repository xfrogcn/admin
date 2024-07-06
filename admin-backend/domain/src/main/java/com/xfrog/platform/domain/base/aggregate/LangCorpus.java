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
public class LangCorpus extends AuditEntity {
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