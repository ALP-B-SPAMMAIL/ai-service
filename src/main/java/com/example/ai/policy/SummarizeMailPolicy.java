package com.example.ai.policy;

import com.example.ai.event.MailNotTaggedSpamEvent;
import com.example.ai.eventDto.MailNotTaggedSpamEventDto;
import com.example.ai.service.AiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class SummarizeMailPolicy {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AiService aiService;

    @KafkaListener(topics = "mail", groupId = "mail-ai-mail-not-tagged-spam")
    public void listen(
            @Header(value = "type", required = false) String type,
            @Payload String data
    ) {
        objectMapper.registerModule(new JavaTimeModule());
        if (type != null && type.equals("MailNotTaggedSpamEvent")) {
            try {
                System.out.println("MailNotTaggedSpamEvent Received");
                MailNotTaggedSpamEvent event = objectMapper.readValue(data, MailNotTaggedSpamEvent.class);
                MailNotTaggedSpamEventDto payload = event.getPayload();
                if (payload != null) {
                    aiService.summarizeMail(payload);
                } else {
                    System.out.println("Warning: Payload is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
    
