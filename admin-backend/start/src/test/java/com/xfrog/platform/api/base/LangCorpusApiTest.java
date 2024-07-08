package com.xfrog.platform.api.base;

import com.xfrog.platform.api.BaseApiTest;
import com.xfrog.platform.api.base.fixtures.LangApiFixtures;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTOFixtures;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LangCorpusApiTest extends BaseApiTest {

    @Autowired
    private LangCorpusDomainRepository langCorpusDomainRepository;
    @Autowired
    private LangDomainRepository langDomainRepository;
    @Autowired
    private LangApiFixtures langApiFixtures;

    @BeforeEach
    void setUp() {

    }

    @Test
    @SneakyThrows
    void createLangCorpus_should_success() {
        CreateLangCorpusRequestDTO requestDTO = LangDTOFixtures.defaultCreateLangCorpusRequestDTO().build();
        request(post("/api/langcorpus", requestDTO))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = {LangApiFixtures.SQL_TRUNCATE_LANG_CORPUS, LangApiFixtures.SQL_TRUNCATE_LANG})
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
    void getLangCorpus_should_success() {
        request(get("/api/langcorpus/1"))
                .andExpect(status().isOk());
    }
    @Test
    @SneakyThrows
    @Sql(statements = LangApiFixtures.SQL_TRUNCATE_LANG_CORPUS)
    void updateLangCorpus_should_success() {
        LangCorpus langCorpus = langApiFixtures.createAndSaveLangCorpus();

        UpdateLangCorpusRequestDTO updateLangCorpusRequestDTO = LangDTOFixtures.defaultUpdateLangCorpusRequestDTO()
                .build();

        request(post("/api/langcorpus/" + langCorpus.getId().toString(), updateLangCorpusRequestDTO))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = LangApiFixtures.SQL_TRUNCATE_LANG_CORPUS)
    void enableLangCorpus_should_success() {
        LangCorpus langCorpus = langApiFixtures.createAndSaveLangCorpus();

        request(post("/api/langcorpus/" + langCorpus.getId().toString() + "/true", null))
                .andExpect(status().isOk());

        request(post("/api/langcorpus/" + langCorpus.getId().toString() + "/false", null))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = LangApiFixtures.SQL_TRUNCATE_LANG_CORPUS)
    void deleteLangCorpus_should_success() {
        LangCorpus langCorpus = langApiFixtures.createAndSaveLangCorpus();

        request(delete("/api/langcorpus/" + langCorpus.getId().toString(), null))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = {LangApiFixtures.SQL_TRUNCATE_LANG_CORPUS, LangApiFixtures.SQL_TRUNCATE_LANG})
    void configLangLocal_should_success() {
        LangCorpus langCorpus = langApiFixtures.createAndSaveLangCorpus();
        Lang lang = langApiFixtures.createAndSaveLang();

        Map<String, String> localMap = Map.of(lang.getCode(), "TEST");

        request(put("/api/langcorpus/" + langCorpus.getId().toString() + "/local", localMap))
                .andExpect(status().isOk());
    }


}
