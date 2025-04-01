package com.example.ai.event;

import com.example.ai.eventDto.MailNotTaggedSpamEventDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailNotTaggedSpamEvent extends AbstractEvent {
    private MailNotTaggedSpamEventDto payload;
    
    // Default constructor for Jackson deserialization
    public MailNotTaggedSpamEvent() {
        super();
    }

    public MailNotTaggedSpamEvent(MailNotTaggedSpamEventDto mailNotTaggedSpamEventDto) {
        super(mailNotTaggedSpamEventDto);
        this.payload = mailNotTaggedSpamEventDto;
    }
}