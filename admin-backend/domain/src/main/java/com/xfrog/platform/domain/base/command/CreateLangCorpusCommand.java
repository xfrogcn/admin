package com.xfrog.platform.domain.base.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateLangCorpusCommand {
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
