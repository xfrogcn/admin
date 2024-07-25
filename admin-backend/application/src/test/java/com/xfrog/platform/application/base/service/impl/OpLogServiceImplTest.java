package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.oplog.domain.OpLog;
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

    @InjectMocks
    private OpLogServiceImpl opLogService;

    @Test
    void saveOpLogs_ShouldSuccess() {
        List<OpLog> logs = List.of(OpLogFixtures.createDefaultOpLog().build());
        opLogService.saveOpLogs(logs);
        verify(opLogDomainRepository, times(1))
                .saveAll(argThat(opLogs -> opLogs == logs));
    }
}