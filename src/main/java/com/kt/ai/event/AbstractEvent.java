package com.kt.ai.event;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.kt.ai.eventDto.AbstractEventDto;

import lombok.Data;

@Data
public abstract class AbstractEvent {
    protected String topic;
    private String eventType;
    
    private AbstractEventDto payload;
    
    private Long timestamp;

    public AbstractEvent() {
        this.eventType = this.getClass().getSimpleName();
        this.timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    public AbstractEvent(AbstractEventDto payload) {
        this.payload = payload;
        this.eventType = this.getClass().getSimpleName();
        this.timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
    }
}
