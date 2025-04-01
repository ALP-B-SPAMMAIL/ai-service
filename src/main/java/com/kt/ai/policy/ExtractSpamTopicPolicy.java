package com.kt.ai.policy;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.kt.ai.event.TopicExtracted;
import com.kt.ai.model.Ai;
import com.kt.ai.repository.AiRepository;
import com.kt.ai.service.OpenAiService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExtractSpamTopicPolicy {
    private final OpenAiService openAiService;
    private final AiRepository aiRepository;

    public TopicExtracted execute(Ai ai) throws IOException {
        // 1. 프롬프트를 OpenAI에 전달
        String result = openAiService.chat(ai.getAiInput());

        // 2. Aggregate 상태 변경
        ai.complete(result);

        // 3. 저장
        Ai saved = aiRepository.save(ai);

        // 4. 도메인 이벤트 반환
        return new TopicExtracted(saved.getId(), result);
    }
}
