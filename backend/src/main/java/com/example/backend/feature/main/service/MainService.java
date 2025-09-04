package com.example.backend.feature.main.service;

import com.example.backend.feature.common.CallApi;
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
 * fileName       : MainService
 * author         : minki-jeon
 * date           : 2025-09-01 (월)
 * description    : API 호출 서비스
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-01 (월)        minki-jeon       최초 생성
 * 2025-09-04 (목)        minki-jeon       Create Translate Text to English
 * 2025-09-04 (목)        minki-jeon       Rename CallApiService
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class MainService {

    private final CallApi callApi;

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
     * 2025/09/04 오후 4:49     minki-jeon         Move to Call Api
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
        String inputText = request.getText();
        // 입력 텍스트를 영어로 번역
        String translateText = translateTextToEng(inputText);

        // TODO RequestBodyDto
        String model = "dall-e-2";
        int n = 1;
        String size = "1024x1024";

        // TODO apiUrl Move to Constant
        // OpenAI DALL-E API 호출
        String apiUrl = "https://api.openai.com/v1/images/generations";

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "prompt", translateText,
                "n", n,
                "size", size
        );

        // Call API openAI
        ResponseEntity<Map> response = callApi.callOpenAi(apiUrl, requestBody);

        // 이미지 URL 추출
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

        return (String) data.get(0).get("url");
    }


    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/04 오후 4:25
     * description    : 사용자로부터 입력받은 텍스트를 영어로 번역
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/04 오후 4:25     minki-jeon         최초 생성.
     * 2025/09/04 오후 4:49     minki-jeon         Move to Call Api
     *
     * </pre>
     *
     * @param text 사용자로부터 입력받은 텍스트
     * @return 번역된 텍스트
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    private String translateTextToEng(String text) {
        // TODO RequestBodyDto
        String addPrompt = "내가 제공한 문장을 영어로 번역해서 제공해주세요. 번역된 문장 외에는 응답하지마세요. ";
        String prompt = addPrompt + "`" + text + "`";
        String model = "gpt-4o-mini-2024-07-18";

        String apiUrl = "https://api.openai.com/v1/responses";

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", prompt
        );

        // Call API openAI
        ResponseEntity<Map> response = callApi.callOpenAi(apiUrl, requestBody);

        List<Map<String, Object>> output = (List<Map<String, Object>>) response.getBody().get("output");
        List<Map<String, Object>> content = (List<Map<String, Object>>) output.get(0).get("content");
        String result = (String) content.get(0).get("text");

        return result;
    }
}
