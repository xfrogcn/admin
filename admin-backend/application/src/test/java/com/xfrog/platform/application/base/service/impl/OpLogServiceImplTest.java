package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.oplog.domain.OpLog;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;
import com.xfrog.platform.application.base.repository.OpLogRepository;
import com.xfrog.platform.domain.base.aggregate.OpLogFixtures;
import com.xfrog.platform.domain.base.repository.OpLogDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OpLogServiceImplTest {

    @Mock
    private OpLogDomainRepository opLogDomainRepository;
    @Mock
    private OpLogRepository opLogRepository;

    @InjectMocks
    private OpLogServiceImpl opLogService;

    @Test
    void saveOpLogs_ShouldSuccess() {
        List<OpLog> logs = List.of(OpLogFixtures.createDefaultOpLog().build());
        opLogService.saveOpLogs(logs);
        verify(opLogDomainRepository, times(1))
                .saveAll(argThat(opLogs -> opLogs == logs));
    }

    @Test
    void listOpLogs_ShouldSuccess() {
        QueryOpLogRequestDTO query =QueryOpLogRequestDTO.builder().build();
        opLogService.listOpLogs(query);
        verify(opLogRepository, times(1))
                .queryBy(argThat(queryDTO -> queryDTO == query));
    }
}