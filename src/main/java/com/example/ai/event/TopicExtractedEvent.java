package com.example.ai.event;

import com.example.ai.eventDto.TopicExtractedEventDto;


public class TopicExtractedEvent extends AbstractEvent {
    private TopicExtractedEventDto payload;
    public TopicExtractedEvent() {
        super();
    }

    public TopicExtractedEvent(TopicExtractedEventDto topicExtractedEventDto) {
        super(topicExtractedEventDto);
        this.payload = topicExtractedEventDto;
    }
}
