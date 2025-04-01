package com.kt.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ai.dto.ChatRequest;
import com.kt.ai.dto.ChatResponse;
import com.kt.ai.eventDto.ReportRequestCreatedEventDto;
import com.kt.ai.service.OpenAiService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private OpenAiService openAiService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) throws Exception {
        ReportRequestCreatedEventDto reportRequestCreatedEventDto = new ReportRequestCreatedEventDto();
        reportRequestCreatedEventDto.setTargetId(Long.parseLong(request.getTargetId()));
        reportRequestCreatedEventDto.setTargetGender(request.getTargetGender());
        reportRequestCreatedEventDto.setTargetAge(request.getTargetAge());
        reportRequestCreatedEventDto.setTargetInterest(request.getTargetInterest());
        reportRequestCreatedEventDto.setTopic(request.getTopic());
        String result = openAiService.createReport(reportRequestCreatedEventDto);
        return new ChatResponse(result);
    }
}
