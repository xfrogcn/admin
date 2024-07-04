package com.xfrog.platform.application.base;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.CreateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "DicApi", description = "字典管理接口")
@RequestMapping("/api/dics")
public interface DicApi {
    @PostMapping
    @Operation(summary = "创建字典")
    Long createDic(@Valid @RequestBody CreateDicRequestDTO dic);

    @PostMapping("/list")
    @Operation(summary = "查询字典列表")
    PageDTO<DicDTO> listDics(@Valid @RequestBody QueryDicRequestDTO queryDTO);

    @GetMapping("/{dicId}")
    @Operation(summary = "查询字典")
    DicDTO getDic(@PathVariable("dicId") Long dicId);

    @PostMapping("/list/by-types")
    @Operation(summary = "根据类型查询字典")
    List<DicDTO> getDicByTypes(@RequestBody List<String> dicTypes);

    @PostMapping("/{dicId}")
    @Operation(summary = "更新字典")
    void updateDic(@PathVariable("dicId") Long dicId, @Valid @RequestBody UpdateDicRequestDTO dic);

    @DeleteMapping("/{dicId}")
    @Operation(summary = "删除字典")
    void deleteDic(@PathVariable("dicId") Long dicId);

    @PostMapping("/{dicId}/items")
    @Operation(summary = "创建字典项")
    Long createDicItem(@PathVariable("dicId") Long dicId, @Valid @RequestBody CreateDicItemRequestDTO requestDTO);

    @PutMapping("/{dicId}/items/{itemId}")
    @Operation(summary = "更新字典项")
    void updateDicItem(@PathVariable("dicId") Long dicId, @PathVariable("itemId") Long itemId, @Valid @RequestBody UpdateDicItemRequestDTO requestDTO);

}
