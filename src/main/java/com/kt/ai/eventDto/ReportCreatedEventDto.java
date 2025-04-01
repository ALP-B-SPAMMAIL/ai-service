package com.kt.ai.eventDto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReportCreatedEventDto extends AbstractEventDto {
    private Long id;
    private String actionBy;
    private String whenActioned;
    private String aiInput;
    private String aiOutput;
}