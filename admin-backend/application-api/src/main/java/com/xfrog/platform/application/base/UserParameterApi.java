package com.xfrog.platform.application.base;

import com.xfrog.platform.application.base.dto.UpdateUserParameterRequestDTO;
import com.xfrog.platform.application.base.dto.UserSettingsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "UserParameterApi", description = "用户参数接口")
@RequestMapping("/api/user-parameters")
public interface UserParameterApi {
    @GetMapping()
    @Operation(summary = "获取用户当前所有配置参数")
    UserSettingsDTO getUserSettings();

    @PutMapping
    @Operation(summary = "更新用户参数")
    void updateUserParameters(@RequestBody @Valid UpdateUserParameterRequestDTO requestDTO);
}
