package com.streamwise.streamwise.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.model}")
    private String groqModel;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getMovieRecommendations(String query) {
        String prompt = """
                You are an expert movie recommendation engine.
                The user is looking for: "%s"
                
                Return a JSON array of exactly 5 movie recommendations.
                Each object must have these exact fields:
                - title: (string) exact movie title
                - year: (number) release year
                - genre: (string) primary genre
                - reason: (string) one sentence explaining why this matches the user's request
                - castHighlight: (string) one notable actor or actress in this film
                - moodTag: (string) one word mood e.g. Thrilling, Heartwarming, Dark, Fun
                
                Return ONLY the raw JSON array. No markdown, no backticks, no explanation.
                Start your response with [ and end with ]
                """.formatted(query);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + groqApiKey);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> requestBody = Map.of(
                "model", groqModel,
                "messages", List.of(message),
                "temperature", 0.7,
                "max_tokens", 1000
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                groqApiUrl,
                entity,
                String.class
        );

        // Parse and extract just the recommendations array
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // Clean markdown fences if present
            content = content.replaceAll("```json|```", "").trim();

            return content;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: " + e.getMessage());
        }
    }
}