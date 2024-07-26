package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.oplog.domain.OpLog;
import com.xfrog.platform.application.base.dto.OpLogDTO;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;
import com.xfrog.platform.application.base.repository.OpLogRepository;
import com.xfrog.platform.application.base.service.OpLogService;
import com.xfrog.platform.domain.base.repository.OpLogDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpLogServiceImpl implements OpLogService {

    private final OpLogDomainRepository opLogDomainRepository;

    private final OpLogRepository opLogRepository;

    @Override
    public void saveOpLogs(List<OpLog> logs) {
        opLogDomainRepository.saveAll(logs);
    }

    @Override
    public PageDTO<OpLogDTO> listOpLogs(QueryOpLogRequestDTO requestDTO) {
        return opLogRepository.queryBy(requestDTO);
    }
}
