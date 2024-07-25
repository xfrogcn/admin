package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.oplog.domain.OpLog;
import com.xfrog.platform.application.base.service.OpLogService;
import com.xfrog.platform.domain.base.repository.OpLogDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpLogServiceImpl implements OpLogService {

    private final OpLogDomainRepository opLogDomainRepository;

    @Override
    public void saveOpLogs(List<OpLog> logs) {
        opLogDomainRepository.saveAll(logs);
    }
}
