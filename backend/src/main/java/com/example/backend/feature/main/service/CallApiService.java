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

/**
 * <pre>
 * packageName    : com.example.backend.feature.main.service
 * fileName       : CallApiService
 * author         : minki-jeon
 * date           : 2025-09-01 (월)
 * description    : API 호출 서비스
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-01 (수)        minki-jeon       최초 생성
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class CallApiService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/01 오후 6:14
     * description    : dall-e-2 모델 이미지 생성
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/01 오후 6:14     minki-jeon         From Controller
     *
     * </pre>
     *
     * @param request dall-e-2 API 요청 Parameter DTO
     * @return 생성 이미지 url
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
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

        // 이미지 URL 추출
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

        return (String) data.get(0).get("url");
    }
}
