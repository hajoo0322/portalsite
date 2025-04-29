package com.portalSite.common.infra;

import com.portalSite.chatbot.repository.AiHelper;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
//TODO: 프롬프트 수정 필요
@Repository
public class OpenAiHelper implements AiHelper {

    @Value("${api.key}")
    private String API_KEY;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final OkHttpClient client = new OkHttpClient();

    @Override
    public String extractKeywords(String userInput) {
        return callOpenAi(
                "사용자의 문장에서 핵심 단어만 추출해줘. 명사와 동사를 위주로 하고, 'output:' 같은 단어는 절대 포함하지 마. 불필요한 설명도 하지 마.",
                "입력: " + userInput
        );
    }

    @Override
    public String refineAnswer(String answerText) {
        return callOpenAi(
                "고객 응대용으로 자연스럽고 정중하게 문장을 정리해줘. 예의 바르고 명확하게 말해줘. 'output:' 같은 단어는 포함하지 마.",
                "다듬을 문장: " + answerText
        );
    }

    private String callOpenAi(String systemPrompt, String userContent) {
        JSONObject systemMessage = new JSONObject()
                .put("role", "system")
                .put("content", systemPrompt);
        JSONObject userMessage = new JSONObject()
                .put("role", "user")
                .put("content", userContent);

        JSONArray messages = new JSONArray(Arrays.asList(systemMessage, userMessage));

        JSONObject json = new JSONObject()
                .put("model", "gpt-4o-mini-2024-07-18")
                .put("messages", messages)
                .put("temperature", 0.3);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Error: " + response.code();
            }

            String responseBody = response.body() != null ? response.body().string() : null;
            if (responseBody == null || responseBody.isEmpty()) {
                return "Error: Empty response";
            }

            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
