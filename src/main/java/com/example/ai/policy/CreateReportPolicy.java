package com.kt.ai.policy;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kt.ai.event.ReportCreatedEvent;
import com.kt.ai.event.ReportRequestCreatedEvent;
import com.kt.ai.eventDto.ReportCreatedEventDto;
import com.kt.ai.eventDto.ReportRequestCreatedEventDto;
import com.kt.ai.service.OpenAiService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateReportPolicy {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private OpenAiService openAiService;

    @KafkaListener(topics = "mail-sender", groupId = "mail-sender-ai-report-request-created")
    public void listen(
            @Header(value = "type", required = false) String type,
            @Payload String data
    ) {
        objectMapper.registerModule(new JavaTimeModule());
        if (type != null && type.equals("ReportRequestCreatedEvent")) {
            try {
                System.out.println("ReportRequestCreatedEvent Received");
                ReportRequestCreatedEvent event = objectMapper.readValue(data, ReportRequestCreatedEvent.class);
                ReportRequestCreatedEventDto payload = event.getPayload();
                if (payload != null) {
                    openAiService.createReport(payload);
                } else {
                    System.out.println("Warning: Payload is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
