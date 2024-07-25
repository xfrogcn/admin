package com.xfrog.framework.oplog;

import com.xfrog.framework.oplog.domain.OpLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventOperationLogPublisherTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private EventOperationLogPublisher publisher;

    @Test
    void publish_EmptyList_NoEventPublished() {
        publisher.publish(Collections.emptyList());

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void publish_NonEmptyList_EventPublished() {
        List<OpLog> opLogs = Collections.singletonList(OpLog.builder().build());
        publisher.publish(opLogs);

        ArgumentCaptor<OperationLogEvent> eventCaptor = ArgumentCaptor.forClass(OperationLogEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        OperationLogEvent event = eventCaptor.getValue();
        assertNotNull(event);
        assertEquals(opLogs, event.getLogs());
    }

    @Test
    void publish_NullList_NoEventPublished() {
        publisher.publish(null);

        verify(eventPublisher, never()).publishEvent(any());
    }
}