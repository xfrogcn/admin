package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.common.SnowflakeUidGenerator;

public class LangFixtures {

    public static final String DEFAULT_APPLICATION = "admin";

    public static Lang.LangBuilder createDefaultLang() {
        return Lang.builder()
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .application(DEFAULT_APPLICATION)
                .code("zh-CN")
                .localName("中文")
                .name("中文")
                .createdBy(1L)
                .enabled(true);
    }

    public static LangCorpus.LangCorpusBuilder createDefaultCorpus() {
        return LangCorpus.builder()
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .application(DEFAULT_APPLICATION)
                .corpusCode("create-button-text")
                .corpusGroup("test")
                .corpusType("test")
                .createdBy(1L)
                .enabled(true);
    }

    public static LangLocal.LangLocalBuilder createDefaultLocal() {
        return LangLocal.builder()
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .application(DEFAULT_APPLICATION)
                .langCode("zh-CN")
                .langCorpusCode("create-button-text")
                .langCorpusId(1L)
                .langId(1L)
                .localValue("创建")
                .createdBy(1L);
    }

    public static LangLocal.LangLocalBuilder createDefaultLocal(LangCorpus langCorpus, Lang lang) {
        return LangLocal.builder()
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .application(langCorpus.getApplication())
                .langCode(lang.getCode())
                .langCorpusCode(langCorpus.getCorpusCode())
                .langCorpusId(langCorpus.getId())
                .langId(lang.getId())
                .localValue("创建")
                .configured(true)
                .createdBy(1L);
    }
}