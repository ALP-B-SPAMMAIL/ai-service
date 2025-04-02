package com.example.ai.eventDto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  
public class ReportCreatedEventDto extends AbstractDto {
    private int id;
    private String report;
    private String targetAddress;
}