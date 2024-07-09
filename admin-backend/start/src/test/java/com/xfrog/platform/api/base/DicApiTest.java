package com.xfrog.platform.api.base;

import com.xfrog.platform.api.BaseApiTest;
import com.xfrog.platform.api.base.fixtures.DicApiFixtures;
import com.xfrog.platform.application.base.dto.CreateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTOFixtures;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.domain.base.aggregate.DicItem;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(DicApiFixtures.class)
public class DicApiTest extends BaseApiTest {

    @Autowired
    private DicApiFixtures dicApiFixtures;

    @BeforeEach
    void setUp() {

    }

    @SneakyThrows
    @Test
    @Sql(statements = {DicApiFixtures.SQL_TRUNCATE_DIC})
    void createDic_ShouldSuccessfully() {
        CreateDicRequestDTO requestDTO = DicDTOFixtures.defaultCreateDicRequestDTO().build();
        request(post("/api/dics", requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {DicApiFixtures.SQL_TRUNCATE_DIC})
    void listDics_ShouldSuccessfully() {
        QueryDicRequestDTO requestDTO = QueryDicRequestDTO.builder()
                .pageNum(1)
                .pageSize(10)
                .build();
        request(post("/api/dics/list", requestDTO))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"pageNum\":\"1\",\"pageSize\":\"10\",\"total\":\"0\",\"pages\":\"0\",\"data\":[]}"));
    }

    @SneakyThrows
    @Test
    @Sql(statements = {DicApiFixtures.SQL_TRUNCATE_DIC, DicApiFixtures.SQL_TRUNCATE_DIC_ITEM})
    void getDic_ShouldSuccessfully() {
        Dic dic = dicApiFixtures.createAndSaveDic();
        DicItem dicItem = dicApiFixtures.createAndSaveDicItem(dic.getId());
        request(get(url("/api/dics/{dicId}", dic.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dicItems[0].value").value(dicItem.getValue()));
    }

    @SneakyThrows
    @Test
    @Sql(statements = {DicApiFixtures.SQL_TRUNCATE_DIC, DicApiFixtures.SQL_TRUNCATE_DIC_ITEM})
    void getDicByTypes_ShouldSuccessfully() {
        Dic dic = dicApiFixtures.createAndSaveDic();
        DicItem dicItem = dicApiFixtures.createAndSaveDicItem(dic.getId());
        request(post("/api/dics/list/by-types", List.of(dic.getType())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value(dic.getType()))
                .andExpect(jsonPath("$[0].dicItems[0].value").value(dicItem.getValue()));
    }

    @SneakyThrows
    @Test
    @Sql(statements = {DicApiFixtures.SQL_TRUNCATE_DIC})
    void updateDic_ShouldSuccessfully() {
        Dic dic = dicApiFixtures.createAndSaveDic();
        UpdateDicRequestDTO requestDTO = DicDTOFixtures.defaultUpdateDicRequestDTO().build();
        request(post(url("/api/dics/{dicId}", dic.getId()), requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {DicApiFixtures.SQL_TRUNCATE_DIC})
    void deleteDic_ShouldSuccessfully() {
        Dic dic = dicApiFixtures.createAndSaveDic();
        request(delete(url("/api/dics/{dicId}" , dic.getId()), null))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {DicApiFixtures.SQL_TRUNCATE_DIC, DicApiFixtures.SQL_TRUNCATE_DIC_ITEM})
    void createDicItem_ShouldSuccessfully() {
        Dic dic = dicApiFixtures.createAndSaveDic();
        CreateDicItemRequestDTO requestDTO = DicDTOFixtures.defaultCreateDicItemRequestDTO().build();
        request(post(url("/api/dics/{dicId}/items", dic.getId()), requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {DicApiFixtures.SQL_TRUNCATE_DIC, DicApiFixtures.SQL_TRUNCATE_DIC_ITEM})
    void updateDicItem_ShouldSuccessfully() {
        Dic dic = dicApiFixtures.createAndSaveDic();
        DicItem dicItem = dicApiFixtures.createAndSaveDicItem(dic.getId());

        UpdateDicItemRequestDTO requestDTO = DicDTOFixtures.defaultUpdateDicItemRequestDTO().build();

        request(put(url("/api/dics/{dicId}/items/{itemId}", dic.getId(), dicItem.getId()), requestDTO))
                .andExpect(status().isOk());
    }

}
