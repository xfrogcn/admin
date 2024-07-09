package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.common.SnowflakeUidGenerator;

public class DicFixtures {
    public static Dic.DicBuilder<?, ?> createDefaultDic() {
        return Dic.builder()
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .name("name")
                .memo("memo")
                .type("type")
                .labelLangCodeExtValue1("labelLangCodeExtValue1")
                .labelLangCodeExtValue2("labelLangCodeExtValue2")
                .labelLangCodeValue("labelLangCodeValue");
    }

    public static DicItem.DicItemBuilder createDefaultDicItem(Long dicId) {
        return DicItem.builder()
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .dicId(dicId)
                .value("value")
                .displayText("displayText")
                .langCode("langCode")
                .extValue1("extValue1")
                .extValue2("extValue2")
                .displayOrder(1)
                .enabled(true)
                .memo("memo");
    }
}
