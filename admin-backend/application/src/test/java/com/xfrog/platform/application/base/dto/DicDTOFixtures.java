package com.xfrog.platform.application.base.dto;

public class DicDTOFixtures {
    public static CreateDicRequestDTO.CreateDicRequestDTOBuilder defaultCreateDicRequestDTO() {
        return CreateDicRequestDTO.builder()
                .name("test")
                .type("test")
                .labelLangCodeValue("test")
                .labelLangCodeExtValue1("test")
                .labelLangCodeExtValue2("test")
                .memo("test");
    }

    public static UpdateDicRequestDTO.UpdateDicRequestDTOBuilder defaultUpdateDicRequestDTO() {
        return UpdateDicRequestDTO.builder()
                .name("test")
                .type("test")
                .labelLangCodeValue("test")
                .labelLangCodeExtValue1("test")
                .labelLangCodeExtValue2("test")
                .memo("test");
    }

    public static CreateDicItemRequestDTO.CreateDicItemRequestDTOBuilder defaultCreateDicItemRequestDTO() {
        return CreateDicItemRequestDTO.builder()
                .value("value")
                .displayOrder(100)
                .displayText("displayText")
                .langCode("langCode")
                .enabled(true)
                .extValue1("extValue1")
                .extValue2("extValues")
                .memo("memo");
    }

    public static UpdateDicItemRequestDTO.UpdateDicItemRequestDTOBuilder defaultUpdateDicItemRequestDTO() {
        return UpdateDicItemRequestDTO.builder()
                .value("value")
                .displayOrder(100)
                .displayText("displayText")
                .langCode("langCode")
                .enabled(true)
                .extValue1("extValue1")
                .extValue2("extValues")
                .memo("memo");
    }
}
