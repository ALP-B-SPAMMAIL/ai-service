package com.kt.ai.event;

import com.kt.ai.eventDto.TopicExtractedEventDto;


public class TopicExtractedEvent extends AbstractEvent {
    public TopicExtractedEvent(TopicExtractedEventDto topicExtractedEventDto) {
        super(topicExtractedEventDto);
        this.topic = "TopicExtract";
    }
}
