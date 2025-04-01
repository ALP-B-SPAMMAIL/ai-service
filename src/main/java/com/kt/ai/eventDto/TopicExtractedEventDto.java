package com.kt.ai.eventDto;

public class TopicExtractedEventDto extends AbstractEventDto {
    private Long id;
    private String aiOutput;

    public TopicExtractedEventDto(Long id, String aiOutput) {
        this.id = id;
        this.aiOutput = aiOutput;
    }
}
