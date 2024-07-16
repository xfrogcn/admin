package com.xfrog.platform.application.base;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Tag(name = "LangCorpusApi", description = "语料库管理接口")
@RequestMapping("/api/langcorpus")
public interface LangCorpusApi {
    @PostMapping
    @Operation(summary = "创建语料库")
    List<Long> createLangCorpus(@Valid @RequestBody CreateLangCorpusRequestDTO langCorpus);

    @PostMapping("/list")
    @Operation(summary = "查询语料库列表")
    PageDTO<LangCorpusDTO> listLangCorpus(@Valid @RequestBody QueryLangCorpusRequestDTO queryDTO);

    @GetMapping("/{langCorpusId}")
    @Operation(summary = "查询语料库")
    LangCorpusDTO getLangCorpus(@PathVariable("langCorpusId") Long langCorpusId);

    @PostMapping("/{langCorpusId}")
    @Operation(summary = "更新语料库")
    void updateLangCorpus(@PathVariable("langCorpusId") Long langCorpusId, @Valid @RequestBody UpdateLangCorpusRequestDTO langCorpus);

    @PostMapping("/{langCorpusId}/{enabled}")
    @Operation(summary = "启用或禁用语种")
    void enableLangCorpus(@PathVariable("langCorpusId") Long langCorpusId, @PathVariable("enabled") Boolean enabled);

    @DeleteMapping("/{langCorpusId}")
    @Operation(summary = "删除语料库")
    void deleteLangCorpus(@Valid @PathVariable("langCorpusId") Long langCorpusId);

    @PutMapping("/{langCorpusId}/local")
    @Operation(summary = "配置语料本地化")
    void configLangLocal(@Valid @PathVariable("langCorpusId") Long langCorpusId, @RequestBody @Valid @NotNull Map<String, String> langLocal);

    @GetMapping("/local/{application}/{langCode}")
    @Operation(summary = "获取指定语种的本地化语言")
    Map<String, String> getLangLocal(
            @PathVariable(name = "application", required = true) String application,
            @PathVariable(name = "langCode", required = true) String langCode);
}
