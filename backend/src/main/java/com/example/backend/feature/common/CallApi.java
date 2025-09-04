package com.example.backend.feature.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * <pre>
 * packageName    : com.example.backend.feature.common
 * fileName       : CallApi
 * author         : minki-jeon
 * date           : 2025-09-04 (목)
 * description    : API 호출
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-04 (목)        minki-jeon       최초 생성
 * </pre>
 */
public class CallApi {

    @Value("${openai.api.key}")
    private static String openaiApiKey;

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/04 오후 4:49
     * description    : OpenAI 호출
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/04 오후 4:49     minki-jeon         최초 생성.
     *
     * </pre>
     *
     * @param apiUrl
     * @param requestBody
     * @return response 호출 결과
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    public static ResponseEntity<Map> callOpenAi(String apiUrl, Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForEntity(apiUrl, entity, Map.class);
    }
}
