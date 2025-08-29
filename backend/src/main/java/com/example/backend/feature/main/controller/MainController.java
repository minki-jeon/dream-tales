package com.example.backend.feature.main.controller;

import com.example.backend.feature.main.dto.ImageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @PostMapping("/create/dalle2")
    public ResponseEntity<Map<String, Object>> generateImage(@RequestBody ImageRequestDto request) {
//        TODO 1. MOVE TO SEVICE
//        TODO 2. Translate request.getText()
//        TODO 3. ADD Initial Prompt
//        TODO 4. GET Delay-Time
        try {
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
            String imageUrl = (String) data.get(0).get("url");

            // S3에 이미지 저장하고 URL 반환
            // TODO S3연동
//            String s3Url = saveImageToS3(imageUrl);
//            return ResponseEntity.ok(Map.of("image_path", s3Url));
            // TODO S3연동하면 삭제
            return ResponseEntity.ok(Map.of("image_path", imageUrl));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "이미지 생성 실패"));
        }
    }


}
