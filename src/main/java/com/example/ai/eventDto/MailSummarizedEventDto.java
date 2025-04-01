package com.example.ai.eventDto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailSummarizedEventDto extends AbstractDto {
    private int mailId;
    private String summary;
}