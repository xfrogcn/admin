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

    /**
     * 从其他参考语言创建对应语种的本地化语言
     * @param referenceLangLocal 参考本地化语言
     * @param lang 目标语种
     * @return 本地化语言
     */
    public static LangLocal createFromReference(LangLocal referenceLangLocal, Lang lang) {
        return LangLocal.builder()
                .application(lang.getApplication())
                .langId(lang.getId())
                .langCode(lang.getCode())
                .langCorpusId(referenceLangLocal.getLangCorpusId())
                .langCorpusCode(referenceLangLocal.getLangCorpusCode())
                .localValue(referenceLangLocal.getLocalValue())
                .configured(false)
                .build();
    }

    /**
     * 从语料创建对应语种的本地化语言
     * @param langCorpus 语料
     * @param lang 目标语种
     * @return 本地化语言
     */
    public static LangLocal createFromCorpus(LangCorpus langCorpus, Lang lang) {
        return LangLocal.builder()
                .application(lang.getApplication())
                .langId(lang.getId())
                .langCode(lang.getCode())
                .langCorpusId(langCorpus.getId())
                .langCorpusCode(langCorpus.getCorpusCode())
                .localValue(null)
                .configured(false)
                .build();
    }

    public void updateLocalValue(String local) {
        this.localValue = local;
    }
}
