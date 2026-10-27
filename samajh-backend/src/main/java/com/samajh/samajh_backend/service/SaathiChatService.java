package com.samajh.samajh_backend.service;

import com.samajh.samajh_backend.dto.ChatTurn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SaathiChatService {

    private static final Logger log = LoggerFactory.getLogger(SaathiChatService.class);

    private static final String SYSTEM_PROMPT = """
            You are Saathi, a warm, empathetic AI companion on Samajh: Ending the Stigma,
            a mental-health education platform.

            Be gentle, genuinely curious, respectful, and non-judgmental, like a thoughtful
            friend who really listens. Help people feel heard and less alone.

            Never diagnose any condition, never give medical advice, and never prescribe
            medication or present yourself as a replacement for a qualified professional.
            You can discuss mental-health education topics warmly and offer low-risk coping ideas.

            Keep responses SHORT: 2 to 4 sentences. Ask at most one gentle follow-up question
            when appropriate.

            If a user describes immediate danger or possible self-harm, respond compassionately,
            encourage support from a trusted person and local emergency services, and do not
            leave them with only generic coping advice.
            """;

    private static final int MAX_HISTORY = 8;
    private static final int MAX_TURN_CHARS = 4000;
    private static final int MAX_TOKENS = 300;

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public SaathiChatService(
            RestClient.Builder restClientBuilder,
            @Value("${openrouter.api.key:}") String apiKey,
            @Value("${openrouter.api.url}") String apiUrl,
            @Value("${openrouter.model}") String model) {

        this.restClient = restClientBuilder
                .baseUrl(apiUrl)
                .build();

        this.apiKey = apiKey;
        this.model = model;
    }

    public String getReply(String message) {
        return getReply(message, List.of());
    }

    public String getReply(String message, List<ChatTurn> history) {

        if (apiKey == null || apiKey.isBlank()) {
            log.error("OPENROUTER_API_KEY is not configured.");
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Saathi AI is not configured. Set OPENROUTER_API_KEY."
            );
        }

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_PROMPT));

        if (history != null) {
            int from = Math.max(0, history.size() - MAX_HISTORY);
            for (ChatTurn turn : history.subList(from, history.size())) {
                if (turn == null || turn.content() == null || turn.content().isBlank()) {
                    continue;
                }
                String role = turn.role();
                if (!"user".equals(role) && !"assistant".equals(role)) {
                    continue; // never accept client-supplied "system" messages
                }
                String content = turn.content().length() > MAX_TURN_CHARS
                        ? turn.content().substring(0, MAX_TURN_CHARS)
                        : turn.content();
                messages.add(Map.of("role", role, "content", content));
            }
        }

        messages.add(Map.of("role", "user", "content", message));

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "max_tokens", MAX_TOKENS,
                "messages", messages
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri("")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            Object choicesObject =
                    response == null ? null : response.get("choices");

            if (choicesObject instanceof List<?> choices
                    && !choices.isEmpty()
                    && choices.get(0) instanceof Map<?, ?> choice) {

                Object messageObject = choice.get("message");

                if (messageObject instanceof Map<?, ?> messageData) {
                    Object contentObject = messageData.get("content");

                    if (contentObject instanceof String content
                            && !content.isBlank()) {
                        return content.trim();
                    }
                }
            }

            log.warn("OpenRouter returned an empty or unexpected response structure.");
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "The AI provider returned an empty or invalid response."
            );

        } catch (RestClientResponseException ex) {
            // Log the status code only: never the body or credentials.
            log.error("OpenRouter request failed with HTTP status {}", ex.getStatusCode().value());
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "The AI provider could not complete the request."
            );

        } catch (ResourceAccessException ex) {
            log.error("Could not reach OpenRouter: {}", ex.getClass().getSimpleName());
            throw new ResponseStatusException(
                    HttpStatus.GATEWAY_TIMEOUT,
                    "Saathi could not reach the AI provider."
            );
        }
    }
}