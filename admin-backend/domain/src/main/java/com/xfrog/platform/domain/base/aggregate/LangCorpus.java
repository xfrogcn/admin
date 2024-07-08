package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import com.xfrog.platform.domain.base.command.CreateLangCorpusCommand;
import com.xfrog.platform.domain.base.command.UpdateLangCorpusCommand;
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

    public static LangCorpus create(CreateLangCorpusCommand command) {
        return LangCorpus.builder()
                .application(command.getApplication())
                .corpusType(command.getCorpusType())
                .corpusGroup(command.getCorpusGroup())
                .corpusCode(command.getCorpusCode())
                .memo(command.getMemo())
                .enabled(command.getEnabled())
                .build();
    }

    public void  update(UpdateLangCorpusCommand command) {
        this.corpusType = command.getCorpusType();
        this.corpusGroup = command.getCorpusGroup();
        this.memo = command.getMemo();
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}