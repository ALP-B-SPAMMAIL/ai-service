package com.example.ai.eventDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailTaggedSpamEventDto extends AbstractDto {
    private int mailId;
    private boolean isSpam;
    private String mailContent;


}
