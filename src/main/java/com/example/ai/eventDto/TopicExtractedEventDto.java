package com.example.ai.eventDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TopicExtractedEventDto extends AbstractDto {
    private int mailId;
    private String topic;

    public TopicExtractedEventDto(int mailId, String topic) {
        this.mailId = mailId;
        this.topic = topic;
    }
}
