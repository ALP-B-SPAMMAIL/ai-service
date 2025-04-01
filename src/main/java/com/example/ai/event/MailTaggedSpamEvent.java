package com.example.ai.event;

import com.example.ai.eventDto.MailTaggedSpamEventDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailTaggedSpamEvent extends AbstractEvent {
    private MailTaggedSpamEventDto payload;
    
    // Default constructor for Jackson deserialization
    public MailTaggedSpamEvent() {
        super();
    }

    public MailTaggedSpamEvent(MailTaggedSpamEventDto mailTaggedSpamEventDto) {
        super(mailTaggedSpamEventDto);
        this.payload = mailTaggedSpamEventDto;
    }
}