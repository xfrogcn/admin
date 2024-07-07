package com.xfrog.platform.application.base;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.CreateLangRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "LangApi", description = "语种管理接口")
@RequestMapping("/api/langs")
public interface LangApi {
    @PostMapping
    @Operation(summary = "创建语言")
    Long createLanguage(@Valid @RequestBody CreateLangRequestDTO language);

    @PostMapping("/list")
    @Operation(summary = "查询语种列表")
    PageDTO<LangDTO> listLanguages(@Valid @RequestBody QueryLangRequestDTO queryDTO);

    @GetMapping("/{langId}")
    @Operation(summary = "查询语言")
    LangDTO getLanguage(@PathVariable("langId") Long langId);

    @PostMapping("/{langId}")
    @Operation(summary = "更新语言")
    void updateLanguage(@PathVariable("langId") Long langId, @Valid @RequestBody UpdateLangRequestDTO language);

    @PostMapping("/{langId}/{enabled}")
    @Operation(summary = "启用或禁用语种")
    void enableLanguage(@PathVariable("langId") Long langId, @PathVariable("enabled") Boolean enabled, @RequestParam("referenceLangId") Long referenceLangId);

    @DeleteMapping("/{langId}")
    @Operation(summary = "删除语言")
    void deleteLanguage(@Valid @PathVariable("langId") Long languageId);
}
