package com.xfrog.platform.application.base.dto;

import com.xfrog.platform.domain.base.aggregate.LangFixtures;

import java.util.Collections;
import java.util.HashMap;

public class LangDTOFixtures {

    public static CreateLangRequestDTO.CreateLangRequestDTOBuilder defaultCreateLangRequestDTO() {
        return CreateLangRequestDTO.builder()
                .application(LangFixtures.DEFAULT_APPLICATION)
                .code("zh-CN")
                .enabled(true)
                .localName("中文")
                .name("中文");
    }

    public static LangDTO.LangDTOBuilder defaultLangDTO() {
        return LangDTO.builder()
                .application(LangFixtures.DEFAULT_APPLICATION)
                .code("zh-CN")
                .enabled(true)
                .id(1L)
                .localName("中文")
                .name("中文");
    }

    public static UpdateLangRequestDTO.UpdateLangRequestDTOBuilder defaultUpdateLangRequestDTO() {
        return UpdateLangRequestDTO.builder()
                .localName("中文")
                .name("中文");
    }

    public static CreateLangCorpusRequestDTO.CreateLangCorpusRequestDTOBuilder defaultCreateLangCorpusRequestDTO() {
        return CreateLangCorpusRequestDTO.builder()
                .application(LangFixtures.DEFAULT_APPLICATION)
                .corpusGroup("test")
                .corpusType("test")
                .corpusItems(Collections.singletonList(defaultLangCorpusItemDTO().build()));
    }

    public static LangCorpusItemDTO.LangCorpusItemDTOBuilder defaultLangCorpusItemDTO() {
        return LangCorpusItemDTO.builder()
                .corpusCode("create-button-text")
                .memo("memo")
                .langLocales(new HashMap<>())
                .enabled(true);
    }

    public static UpdateLangCorpusRequestDTO.UpdateLangCorpusRequestDTOBuilder defaultUpdateLangCorpusRequestDTO() {
        return UpdateLangCorpusRequestDTO.builder()
                .corpusGroup("test")
                .corpusType("test")
                .memo("test");
    }
}
