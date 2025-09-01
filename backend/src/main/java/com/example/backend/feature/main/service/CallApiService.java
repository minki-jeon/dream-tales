package com.example.backend.feature.main.service;

import com.example.backend.feature.main.dto.ImageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CallApiService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public String createImageOnDallE2(ImageRequestDto request) {
        // OpenAI DALL-E API 호출
        String apiUrl = "https://api.openai.com/v1/images/generations";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "dall-e-2",
                "prompt", request.getText(),
                "n", 1,
                "size", "1024x1024"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

        // 이미지 URL 추출 및 S3 저장
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

        return (String) data.get(0).get("url");
    }
}
