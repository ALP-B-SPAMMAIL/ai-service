package com.example.ai.eventDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailNotTaggedSpamEventDto extends AbstractDto {
    private int mailId;
    private boolean isSpam;
    private String mailContent;
}
