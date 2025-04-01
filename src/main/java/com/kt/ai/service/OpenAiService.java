package com.kt.ai.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kt.ai.event.ReportCreatedEvent;
import com.kt.ai.eventDto.ReportCreatedEventDto;
import com.kt.ai.eventDto.ReportRequestCreatedEventDto;
import com.kt.ai.kafka.KafkaProducer;
import com.kt.ai.model.Ai;
import com.kt.ai.repository.AiRepository;

import lombok.RequiredArgsConstructor;
import okhttp3.*;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final String API_KEY = "***REMOVED***";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private final KafkaProducer kafkaProducer;
    private final AiRepository aiRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String createReport(ReportRequestCreatedEventDto reportRequestCreatedEventDto) throws IOException {
        
        String prompt = "다음의 정보를 가지고 맞춤형 월간 보안 리포트를 작성해주세요. "
        + "정보는 다음과 같습니다. \n"
        + "1. 악성 메일 주요 토픽 정보: " + reportRequestCreatedEventDto.getTopic()
        + "2. 수신자 성별: " + reportRequestCreatedEventDto.getTargetGender()
        + "3. 수신자 나이: " + reportRequestCreatedEventDto.getTargetAge()
        + "4. 수신자 관심사: " + reportRequestCreatedEventDto.getTargetInterest();

        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();

        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        messages.add(message);

        JsonObject body = new JsonObject();
        body.addProperty("model", "gpt-3.5-turbo");
        body.add("messages", messages);

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JsonObject json = gson.fromJson(responseBody, JsonObject.class);
            String result = json.getAsJsonArray("choices")
                       .get(0).getAsJsonObject()
                       .getAsJsonObject("message")
                       .get("content").getAsString();


            ReportCreatedEventDto reportCreatedEventDto = new ReportCreatedEventDto();
            reportCreatedEventDto.setId(reportRequestCreatedEventDto.getTargetId());
            reportCreatedEventDto.setActionBy("REPORT_REQUEST");
            reportCreatedEventDto.setWhenActioned(LocalDateTime.now().toString());
            reportCreatedEventDto.setAiInput(prompt);
            reportCreatedEventDto.setAiOutput(result);

            ReportCreatedEvent reportCreatedEvent = new ReportCreatedEvent(reportCreatedEventDto);
            kafkaProducer.publish(reportCreatedEvent);
            return objectMapper.writeValueAsString(reportCreatedEventDto);
        }
    }




    public String chat(String prompt) throws IOException {
        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();

        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        messages.add(message);

        JsonObject body = new JsonObject();
        body.addProperty("model", "gpt-3.5-turbo");
        body.add("messages", messages);

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JsonObject json = gson.fromJson(responseBody, JsonObject.class);
            return json.getAsJsonArray("choices")
                       .get(0).getAsJsonObject()
                       .getAsJsonObject("message")
                       .get("content").getAsString();
        }
    }
}