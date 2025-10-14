package com.example.backend;

import com.example.backend.feature.common.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 * packageName    : com.example.backend
 * fileName       : CallApiTest
 * author         : minki-jeon
 * date           : 2025-10-14 (화)
 * description    :
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-10-14 (화)        minki-jeon       최초 생성
 * </pre>
 */
@SpringBootTest
@DisplayName("Api 호출 테스트")
public class CallApiTest {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Test
    @DisplayName("OpenAI-GPT-4o-mini 호출 테스트")
    void callApi() {
        String model = Constants.MODEL_OPENAI_GPT_4O_MINI;
        String prompt = "동작 테스트 진행";
        String apiUrl = Constants.API_URL_OPENAI_RESPONSES;

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", prompt
        );

        // set header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        // call api
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
        int statCd = response.getStatusCode().value();
        assertEquals(200, statCd);
        System.out.println("API 응답 상태코드: " + statCd);
    }
}
