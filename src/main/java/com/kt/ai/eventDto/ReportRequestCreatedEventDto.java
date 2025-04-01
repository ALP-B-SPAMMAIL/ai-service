package com.kt.ai.eventDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequestCreatedEventDto extends AbstractEventDto {
    private Long targetId;
    private String topic;
    private String targetGender;
    private String targetAge;
    private String targetInterest;
    
}
