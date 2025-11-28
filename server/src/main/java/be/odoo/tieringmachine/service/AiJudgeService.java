package be.odoo.tieringmachine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.odoo.tieringmachine.web.dto.ai.AiVerdict;
import be.odoo.tieringmachine.web.dto.ai.ChatRequest;
import be.odoo.tieringmachine.web.dto.ai.ChatResponse;
import be.odoo.tieringmachine.web.dto.ai.Message;

@Service
public class AiJudgeService {
        private final RestClient restClient;
        private final ObjectMapper objectMapper;

        @Value("${groq.api.key}")
        private String apiKey;

        public AiJudgeService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
                this.restClient = restClientBuilder
                                .baseUrl("https://api.groq.com/openai/v1")
                                .build();
                this.objectMapper = objectMapper;
        }

        public AiVerdict judgeReport(String reason) {
                String systemPrompt = """
                                You are the AI Judge of a dystopian Social Credit System.
                                Analyze the report reason.
                                Decide the points for the Reporter (reward for snitching) and the Target (penalty).

                                - Minor infractions: Target loses 10-30 points.
                                - Major infractions: Target loses 50-100 points.
                                - False/Spam reports: Punish the Reporter (negative points).

                                You must respond ONLY with valid JSON in this format:
                                {
                                  "reporterPoints": <integer>,
                                  "targetPoints": <integer>
                                }
                                Do not include markdown formatting like ```json.
                                """;
                ChatRequest request = new ChatRequest(
                                "llama-3.1-8b-instant",
                                List.of(
                                                new Message("system", systemPrompt),
                                                new Message("user", "Report Reason: " + reason)),
                                0.8,
                                false);

                ChatResponse response = restClient.post()
                                .uri("/chat/completions")
                                .header("Authorization", "Bearer " + apiKey)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(request)
                                .retrieve()
                                .body(ChatResponse.class);

                try {
                        if (response == null || response.choices() == null || response.choices().isEmpty()) {
                                throw new RuntimeException("Empty response from AI");
                        }
                        String content = response.choices().get(0).message().content();
                        return objectMapper.readValue(content, AiVerdict.class);
                } catch (Exception e) {
                        System.err.println("AI Judgement failed: " + e.getMessage());
                        return new AiVerdict(10, -10);
                }
        }
}
