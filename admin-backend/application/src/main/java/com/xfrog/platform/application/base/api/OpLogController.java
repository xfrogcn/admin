package com.xfrog.platform.application.base.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.OpLogApi;
import com.xfrog.platform.application.base.dto.OpLogDTO;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;
import com.xfrog.platform.application.base.service.OpLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpLogController implements OpLogApi {

    private final OpLogService opLogService;

    @Override
    public PageDTO<OpLogDTO> listOpLogs(QueryOpLogRequestDTO queryDTO) {
        return opLogService.listOpLogs(queryDTO);
    }
}
