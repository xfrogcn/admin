package com.xfrog.framework.common;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class EventPublisher implements ApplicationEventPublisherAware {

    public static EventPublisher INSTANCE = null;

    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        INSTANCE = this;
    }


    public static void publishEvent(ApplicationEvent event) {
        if (INSTANCE == null) {
            return;
        }
        INSTANCE.applicationEventPublisher.publishEvent(event);
    }
}
