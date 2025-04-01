package com.kt.ai.event;

import com.kt.ai.eventDto.ReportRequestCreatedEventDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestCreatedEvent extends AbstractEvent {
    private ReportRequestCreatedEventDto payload;

    public ReportRequestCreatedEvent() {
        super();
        this.topic = "ReportRequestCreated";
    }

    public ReportRequestCreatedEvent(ReportRequestCreatedEventDto payload) {
        super(payload);
        this.payload = payload;
        this.topic = "ReportRequestCreated";
    }
        
}
