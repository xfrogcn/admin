package com.xfrog.platform.application.base;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.OpLogDTO;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "OpLogApi", description = "操作日志管理")
@RequestMapping("/api/oplogs")
public interface OpLogApi {
    @PostMapping("/list")
    @Operation(summary = "查询操作日志")
    PageDTO<OpLogDTO> listOpLogs(@Valid @RequestBody QueryOpLogRequestDTO queryDTO);

}
