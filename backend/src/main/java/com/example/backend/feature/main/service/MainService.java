package com.example.backend.feature.main.service;

import com.example.backend.feature.common.CallApi;
import com.example.backend.feature.common.Constants;
import com.example.backend.feature.main.dto.ImageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
 * 2025-09-08 (월)        minki-jeon       Delete @Value ApiKey
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class MainService {

    private final CallApi callApi;

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
     * 2025/09/05 오후 5:35     minki-jeon         Constants model, apiUrl
     * 2025/09/09 오전 9:51     minki-jeon         Add Image Prompt on Input-text
     * 2025/09/09 오후 12:36    minki-jeon         createImageOnDallE2 메소드명 변경
     *
     * </pre>
     *
     * @param request dall-e-2 API 요청 Parameter DTO
     * @return 생성 이미지 url
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    public String createImage(ImageRequestDto request) {
        String inputText = request.getText();
        // 입력 텍스트를 영어로 번역
        String text = Constants.PROMPT_CREATE_IMAGE_HEADER_DETAIL
                + translateTextToEng(inputText)
                + Constants.PROMPT_CREATE_IMAGE_OPTIMIZATION_DETAIL;

        String model = Constants.MODEL_OPENAI_GPT_IMAGE_1;
        int n = 1;
        String size = "1024x1024";

        // OpenAI DALL-E API 호출
        String apiUrl = Constants.API_URL_OPENAI_RESPONSES_IMAGES;

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "prompt", text,
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
     * 2025/09/05 오후 5:35     minki-jeon         Constants model, apiUrl
     * 2025/09/09 오전 9:51     minki-jeon         Edit addPrompt
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
        String prompt = "`" + text + "` " + Constants.PROMPT_TRANSLATE_INPUT;
        String model = Constants.MODEL_OPENAI_GPT_4O_MINI;

        String apiUrl = Constants.API_URL_OPENAI_RESPONSES;

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
