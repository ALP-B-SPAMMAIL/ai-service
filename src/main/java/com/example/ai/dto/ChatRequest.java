package com.example.ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private String targetId;
    private String targetGender;
    private String targetAge;
    private String targetInterest;
    private String topic;
}
