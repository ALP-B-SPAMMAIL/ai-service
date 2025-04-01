package com.example.ai.event;

import com.example.ai.eventDto.ReportCreatedEventDto;

public class ReportCreatedEvent extends AbstractEvent {
    private ReportCreatedEventDto payload;

    public ReportCreatedEvent() {
        super();
    }

    public ReportCreatedEvent(ReportCreatedEventDto payload) {
        super(payload);
        this.payload = payload;
    }

    
}