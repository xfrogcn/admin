package com.xfrog.platform.domain.base.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLangCorpusCommand {
    // 语料类别
    private String corpusType;
    // 语料分组
    private String corpusGroup;
    // 语料说明
    private String memo;
}
