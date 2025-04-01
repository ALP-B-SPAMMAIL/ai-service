package com.example.ai.policy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.ai.event.ReportRequestCreatedEvent;
import com.example.ai.eventDto.ReportRequestCreatedEventDto;
import com.example.ai.service.AiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateReportPolicy {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AiService aiService;

    @KafkaListener(topics = "mail", groupId = "mail-sender-ai-report-request-created")
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
                    aiService.createReport(payload);
                } else {
                    System.out.println("Warning: Payload is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
