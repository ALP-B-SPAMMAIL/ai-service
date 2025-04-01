package com.example.ai.event;

import com.example.ai.eventDto.MailSummarizedEventDto;

public class MailSummarizedEvent extends AbstractEvent {
    private MailSummarizedEventDto payload;

    public MailSummarizedEvent() {
        super();
    }

    public MailSummarizedEvent(MailSummarizedEventDto payload) {
        super(payload);
        this.payload = payload;
    }

    
}