package com.kt.ai.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopicExtracted {
    private Long id;
    private String aiOutput;
}
