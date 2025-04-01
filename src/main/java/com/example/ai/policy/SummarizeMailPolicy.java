package com.kt.ai.policy;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.kt.ai.model.Ai;
import com.kt.ai.repository.AiRepository;
import com.kt.ai.service.OpenAiService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SummarizeMailPolicy {
    private final OpenAiService openAiService;
    private final AiRepository aiRepository;

    public String execute(Ai ai) throws IOException {
        // 프롬프트를 OpenAI에 보내서 요약 응답 받기
        String summary = openAiService.chat(
            "다음 메일 내용을 간단히 요약해줘:\n\n" + ai.getAiInput()
        );

        // 결과를 Aggregate에 저장
        ai.complete(summary);
        aiRepository.save(ai);

        return summary;
    }
}
