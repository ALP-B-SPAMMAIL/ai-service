package com.kt.ai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ai.dto.ChatRequest;
import com.kt.ai.event.TopicExtracted;
import com.kt.ai.model.Ai;
import com.kt.ai.policy.CreateReportPolicy;
import com.kt.ai.policy.ExtractSpamTopicPolicy;
import com.kt.ai.policy.SummarizeMailPolicy;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    private final ExtractSpamTopicPolicy extractSpamTopicPolicy;
    private final CreateReportPolicy createReportPolicy;
    private final SummarizeMailPolicy summarizeMailPolicy;

    @PostMapping("/topic")
    public TopicExtracted extractSpamTopic(@RequestBody ChatRequest request) throws IOException {
        // 요청에서 prompt와 사용자 정보 세팅
        Ai ai = Ai.builder()
                .actionBy("tester")               // 사용자 ID 등 (추후 로그인 정보 등으로 확장 가능)
                .aiInput(request.getPrompt())     // 프롬프트 입력
                .build();

        return extractSpamTopicPolicy.execute(ai);
    }

    @PostMapping("/mail-summary")
    public String summarizeMail(@RequestBody ChatRequest request) throws IOException {
        Ai ai = Ai.builder()
            .actionBy("tester")
            .aiInput(request.getPrompt())
            .build();

        return summarizeMailPolicy.execute(ai);
    }

    @GetMapping("/report")
    public String createReport() throws IOException {
        return createReportPolicy.execute();
    }
    

}
