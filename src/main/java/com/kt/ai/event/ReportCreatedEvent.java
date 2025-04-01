package com.kt.ai.event;

import com.kt.ai.eventDto.ReportCreatedEventDto;

public class ReportCreatedEvent extends AbstractEvent {
    private ReportCreatedEventDto payload;

    public ReportCreatedEvent() {
        super();
        this.topic = "ReportCreated";
    }

    public ReportCreatedEvent(ReportCreatedEventDto payload) {
        super(payload);
        this.payload = payload;
        this.topic = "ReportCreated";
    }

    
}