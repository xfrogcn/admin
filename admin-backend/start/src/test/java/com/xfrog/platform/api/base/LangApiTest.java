package com.xfrog.platform.api.base;

import com.xfrog.platform.api.base.fixtures.BaseApiFixtures;
import com.xfrog.platform.application.base.dto.CreateLangRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTOFixtures;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangRequestDTO;
import com.xfrog.platform.domain.base.aggregate.Lang;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LangApiTest extends BaseBaseApiTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS, BaseApiFixtures.SQL_TRUNCATE_LANG})
    void createLanguage_should_success() {
        CreateLangRequestDTO requestDTO = LangDTOFixtures.defaultCreateLangRequestDTO().build();
        request(post("/api/langs", requestDTO))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG})
    void listLanguages_should_success() {
        QueryLangRequestDTO requestDTO = QueryLangRequestDTO.builder()
                .pageNum(1)
                .pageSize(10)
                .build();
        request(post("/api/langs/list", requestDTO))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"pageNum\":\"1\",\"pageSize\":\"10\",\"total\":\"0\",\"pages\":\"0\",\"data\":[]}"));
    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG})
    void getLanguage_should_success() {
        request(get("/api/langs/1"))
                .andExpect(status().isOk());
    }
    @Test
    @SneakyThrows
    @Sql(statements = BaseApiFixtures.SQL_TRUNCATE_LANG)
    void updateLanguage_should_success() {
        Lang lang = baseApiFixtures.createAndSaveLang();

        UpdateLangRequestDTO updateLangRequestDTO = LangDTOFixtures.defaultUpdateLangRequestDTO()
                .build();

        request(post(url("/api/langs/{langId}", lang.getId()), updateLangRequestDTO))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG_CORPUS, BaseApiFixtures.SQL_TRUNCATE_LANG})
    void enableLanguage_should_success() {
        Lang lang = baseApiFixtures.createAndSaveLang();

        request(post(url("/api/langs/{langId}/true", lang.getId()), null))
                .andExpect(status().isOk());

        request(post(url("/api/langs/{langId}/false", lang.getId()), null))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = BaseApiFixtures.SQL_TRUNCATE_LANG)
    void deleteLanguage_should_success() {
        Lang lang = baseApiFixtures.createAndSaveLang();

        request(delete(url("/api/langs/{langId}", lang.getId()), null))
                .andExpect(status().isOk());
    }
}
