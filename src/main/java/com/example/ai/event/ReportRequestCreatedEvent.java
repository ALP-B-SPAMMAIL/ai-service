package com.example.ai.event;

import com.example.ai.eventDto.ReportRequestCreatedEventDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestCreatedEvent extends AbstractEvent {
    private ReportRequestCreatedEventDto payload;

    public ReportRequestCreatedEvent() {
        super();
    }

    public ReportRequestCreatedEvent(ReportRequestCreatedEventDto payload) {
        super(payload);
        this.payload = payload;
    }
        
}
