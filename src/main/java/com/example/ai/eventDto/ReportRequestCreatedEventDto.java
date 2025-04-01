package com.example.ai.eventDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequestCreatedEventDto extends AbstractDto {
    private int targetId;
    private String topic;
    private String targetGender;
    private String targetAge;
    private String targetInterest;
    
}
