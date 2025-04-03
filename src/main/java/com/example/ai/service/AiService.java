package com.example.ai.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ai.event.MailSummarizedEvent;
import com.example.ai.event.ReportCreatedEvent;
import com.example.ai.event.TopicExtractedEvent;
import com.example.ai.eventDto.MailNotTaggedSpamEventDto;
import com.example.ai.eventDto.MailSummarizedEventDto;
import com.example.ai.eventDto.MailTaggedSpamEventDto;
import com.example.ai.eventDto.ReportCreatedEventDto;
import com.example.ai.eventDto.ReportRequestCreatedEventDto;
import com.example.ai.eventDto.TopicExtractedEventDto;
import com.example.ai.kafka.KafkaProducer;
import com.example.ai.model.Ai;
import com.example.ai.repository.AiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import okhttp3.*;

@Service
@RequiredArgsConstructor
public class AiService {
    private final String API_KEY = "**REMOVED**";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private final KafkaProducer kafkaProducer;
    private final AiRepository aiRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public String createReport(ReportRequestCreatedEventDto reportRequestCreatedEventDto) throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        String prompt = "다음의 정보를 가지고 맞춤형 월간 보안 리포트를 작성해주세요. "
        + "정보는 다음과 같습니다. \n"
        + "1. 최근 한 달간 발송된 메일 주요 스팸메일들의 정보: " + reportRequestCreatedEventDto.getTopic() + "\n"
        + "2. 수신자 성별: " + reportRequestCreatedEventDto.getTargetGender() + "\n"
        + "3. 수신자 나이: " + reportRequestCreatedEventDto.getTargetAge() + "\n"
        + "4. 수신자 관심사: " + reportRequestCreatedEventDto.getTargetInterest() + "\n\n"
        + "출력 형식은 `반드시` 아래와 같은 JSON 형식으로 출력해주세요.\n"
        + "리포트 내용은 HTML 형식으로 작성하며, 모든 내용은 수신자의 관심사에 맞게 작성해주세요.\n"
        + "리포트는 500자 정도의 분량이어야 하며, 최근 유행하는 스팸 메일에 대한 통계적 분석과\n"
        + "수신자의 정보를 고려하여 특별히 주의 해야할 메일들에 대해 간단한 경고를 포함해야 합니다.\n"
        + "아래의 {{리포트 내용}} 은 하나의 변수로서, 생성한 리포트의 내용을 해당 변수와 치환하여 출력해주세요.\n"
        + "{\n"
        + "    \"report\": \"<html>{{리포트 내용}}</html>\"\n"
        + "}\n\n";




        String result = chat(prompt);
        String report = result.split("\"")[3];
        report = report.replace("\\n", "<br>");

        ReportCreatedEventDto reportCreatedEventDto = new ReportCreatedEventDto();
        reportCreatedEventDto.setId(reportRequestCreatedEventDto.getTargetId());
        reportCreatedEventDto.setTargetAddress(reportRequestCreatedEventDto.getTargetAddress());
        reportCreatedEventDto.setReport(report);


        Ai ai = Ai.builder()
            .actionBy("REPORT_REQUEST")
            .whenActioned(LocalDateTime.now())
            .aiInput(prompt)
            .aiOutput(result)
            .build();
        aiRepository.save(ai);

        ReportCreatedEvent reportCreatedEvent = new ReportCreatedEvent(reportCreatedEventDto);
        kafkaProducer.publish(reportCreatedEvent);
        return objectMapper.writeValueAsString(reportCreatedEventDto);
    }
    
    @Transactional
    public String extractSpamTopic(MailTaggedSpamEventDto mailTaggedSpamEventDto) throws IOException {
        String prompt = "다음의 정보를 가지고 메일의 주요 토픽이 무엇인지 판별해주세요. \n"
        + "토픽은 `반드시` 아래의 목록중의 하나로 매핑해주세요. \n"
        + "목록은 다음과 같습니다. \n"
        + "금융 관련 메일, 알림 및 통지, 배송 및 물류, 문서 및 보이스메일, 인사 및 직원 관련, IT 및 기술 지원, 결제 상태 및 청구서, 직원 교육 및 훈련, 법률 및 계약, 직원 급여 및 보너스 \n\n"
        + "수신한 정보는 다음과 같습니다. \n"
        + "1. 메일 내용: " + mailTaggedSpamEventDto.getMailContent() + "\n\n"
        + "출력 형식은 `반드시` 아래와 같은 JSON 형식으로 출력해주세요.\n"
        + "{\n"
        + "    \"topic\": \"금융 관련 메일\"\n"
        + "}\n\n";

        List<String> topics = Arrays.asList(
            "금융 관련 메일", 
            "알림 및 통지", 
            "배송 및 물류", 
            "문서 및 보이스메일", 
            "인사 및 직원 관련", 
            "IT 및 기술 지원", 
            "결제 상태 및 청구서", 
            "직원 교육 및 훈련", 
            "법률 및 계약", 
            "직원 급여 및 보너스"
            );
            
        String result = chat(prompt);
        String topic = result.split("\"")[3];
        if (!topics.contains(topic)) {
            topic = "기타";
        }

        Ai ai = Ai.builder()
            .actionBy("MAIL_TAGGED_SPAM")
            .whenActioned(LocalDateTime.now())
            .aiInput(prompt)
            .aiOutput(result)
            .build();
        aiRepository.save(ai);
        TopicExtractedEventDto topicExtractedEventDto = new TopicExtractedEventDto(mailTaggedSpamEventDto.getMailId(), topic);
        TopicExtractedEvent topicExtractedEvent = new TopicExtractedEvent(topicExtractedEventDto);
        kafkaProducer.publish(topicExtractedEvent);
        return topic;
    }

    @Transactional
    public String summarizeMail(MailNotTaggedSpamEventDto mailNotTaggedSpamEventDto) throws IOException {
        String prompt = "다음의 정보를 가지고 메일을 한줄에서 두줄 사이로 요약해주세요. \n"
        + "정보는 다음과 같습니다. \n"
        + "1. 메일 내용: " + mailNotTaggedSpamEventDto.getMailContent() + "\n\n"
        + "출력 형식은 `반드시` 아래와 같은 JSON 형식으로 출력해주세요.\n"
        + "내용 판별이 힘든 경우 `내용 요약 불가` 로 출력해주세요.\n"
        + "{\n"
        + "    \"summary\": \"메일 요약\"\n"
        + "}\n\n";

        String result = chat(prompt);
        String summary = result.split("\"")[3];

        Ai ai = Ai.builder()
            .actionBy("MAIL_NOT_TAGGED_SPAM")
            .whenActioned(LocalDateTime.now())
            .aiInput(prompt)
            .aiOutput(result)
            .build();
        aiRepository.save(ai);
        MailSummarizedEventDto mailSummarizedEventDto = new MailSummarizedEventDto();
        mailSummarizedEventDto.setMailId(mailNotTaggedSpamEventDto.getMailId());
        mailSummarizedEventDto.setSummary(summary);
        MailSummarizedEvent mailSummarizedEvent = new MailSummarizedEvent(mailSummarizedEventDto);

        kafkaProducer.publish(mailSummarizedEvent);
        return summary;
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