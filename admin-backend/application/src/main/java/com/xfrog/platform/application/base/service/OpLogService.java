package com.xfrog.platform.application.base.service;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.oplog.domain.OpLog;
import com.xfrog.platform.application.base.dto.OpLogDTO;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;

import java.util.List;

public interface OpLogService {
    void saveOpLogs(List<OpLog> logs);

    PageDTO<OpLogDTO> listOpLogs(QueryOpLogRequestDTO requestDTO);
}
