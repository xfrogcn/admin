package com.xfrog.platform.api.base;

import com.xfrog.platform.api.base.fixtures.BaseApiFixtures;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTOFixtures;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.aggregate.LangFixtures;
import com.xfrog.platform.domain.base.aggregate.LangLocal;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LangCorpusApiTest extends BaseBaseApiTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS, BaseApiFixtures.SQL_TRUNCATE_LANG})
    void createLangCorpus_should_success() {
        CreateLangCorpusRequestDTO requestDTO = LangDTOFixtures.defaultCreateLangCorpusRequestDTO().build();
        request(post("/api/langcorpus", requestDTO))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS, BaseApiFixtures.SQL_TRUNCATE_LANG})
    void listLangCorpus_should_success() {
        QueryLangCorpusRequestDTO requestDTO = QueryLangCorpusRequestDTO.builder()
                .pageNum(1)
                .pageSize(10)
                .build();
        request(post("/api/langcorpus/list", requestDTO))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"pageNum\":\"1\",\"pageSize\":\"10\",\"total\":\"0\",\"pages\":\"0\",\"data\":[]}"));
    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS, BaseApiFixtures.SQL_TRUNCATE_LANG})
    void getLangCorpus_should_success() {
        Lang lang = baseApiFixtures.saveLang(LangFixtures.createDefaultLang()
                .code("zh-CN")
                .name("中文")
                .build());
        LangCorpus langCorpus = baseApiFixtures.createAndSaveLangCorpus();
        LangLocal langLocal = baseApiFixtures.saveLangLocale(LangFixtures.createDefaultLocal(langCorpus, lang)
                .localValue("测试")
                .build());


        request(get(url("/api/langcorpus/{id}", langCorpus.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.langLocales['zh-CN']").value("测试"));
    }
    @Test
    @SneakyThrows
    @Sql(statements = BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS)
    void updateLangCorpus_should_success() {
        LangCorpus langCorpus = baseApiFixtures.createAndSaveLangCorpus();

        UpdateLangCorpusRequestDTO updateLangCorpusRequestDTO = LangDTOFixtures.defaultUpdateLangCorpusRequestDTO()
                .build();

        request(post(url("/api/langcorpus/{langCorpusId}", langCorpus.getId()), updateLangCorpusRequestDTO))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS, BaseApiFixtures.SQL_TRUNCATE_LANG})
    void enableLangCorpus_should_success() {
        LangCorpus langCorpus = baseApiFixtures.createAndSaveLangCorpus();

        request(post(url("/api/langcorpus/{langCorpusId}/true", langCorpus.getId().toString()), null))
                .andExpect(status().isOk());

        request(post(url("/api/langcorpus/{langCorpusId}/false", langCorpus.getId().toString()), null))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS)
    void deleteLangCorpus_should_success() {
        LangCorpus langCorpus = baseApiFixtures.createAndSaveLangCorpus();

        request(delete(url("/api/langcorpus/{langCorpusId}", langCorpus.getId()), null))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS, BaseApiFixtures.SQL_TRUNCATE_LANG})
    void configLangLocal_should_success() {
        LangCorpus langCorpus = baseApiFixtures.createAndSaveLangCorpus();
        Lang lang = baseApiFixtures.createAndSaveLang();

        Map<String, String> localMap = Map.of(lang.getCode(), "TEST");

        request(put(url("/api/langcorpus/{langCorpusId}/local", langCorpus.getId()), localMap))
                .andExpect(status().isOk());
    }


}
