package com.kt.ai.policy;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.kt.ai.service.OpenAiService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateReportPolicy {
    private final OpenAiService openAiService;

    public String execute() throws IOException {
        // 🔧 테스트용 더미 토픽 데이터
        List<String> dummyTopics = List.of(
            "계정 보안", "비정상 로그인", "피싱 링크", "정보 유출", "긴급 확인 요청"
        );

        String prompt = "다음 보안 관련 키워드들을 분석하여 피싱 메일에 관한 잡지에 들어갈 가벼운 느낌의 보안 리포트를 작성해줘:\n" +
                        String.join(", ", dummyTopics);

        return openAiService.chat(prompt);
    }
}
