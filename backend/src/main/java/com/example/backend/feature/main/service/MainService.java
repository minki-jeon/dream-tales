package com.example.backend.feature.main.service;

import com.example.backend.feature.common.CallApi;
import com.example.backend.feature.common.Constants;
import com.example.backend.feature.common.Utils;
import com.example.backend.feature.common.entity.Image;
import com.example.backend.feature.common.repository.ApiLogRepository;
import com.example.backend.feature.common.repository.ImageRepository;
import com.example.backend.feature.main.dto.FourPanelRequestDto;
import com.example.backend.feature.main.dto.ImageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
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
 * 2025-09-10 (수)        minki-jeon       생성이미지 S3 저장, DB 기록
 * 2025-09-14 (일)        minki-jeon       s3UrlPrefix 변수명 변경
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class MainService {

    private final ImageRepository imageRepository;
    private final ApiLogRepository apiLogRepository;
    @Value("${aws.s3.url.prefix}")
    private String s3UrlPrefix;
    @Value("${object.url.image}")
    private String objectUrlImage;

    private final CallApi callApi;
    private final Utils utils;

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/01 오후 6:14
     * description    : 이미지 생성
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/01 오후 6:14     minki-jeon         From Controller
     * 2025/09/04 오후 4:49     minki-jeon         Move to Call Api
     * 2025/09/05 오후 5:35     minki-jeon         Constants model, apiUrl
     * 2025/09/09 오전 9:51     minki-jeon         Add Image Prompt on Input-text
     * 2025/09/09 오후 12:36    minki-jeon         createImageOnDallE2 메소드명 변경
     * 2025/09/09 오후 15:15    minki-jeon         API 응답 Base64 Decoding, Save Image
     * 2025/09/10 오후 12:15    minki-jeon         생성 이미지 s3 저장, db 기록
     * 2025/09/14 오후 1:12     minki-jeon         상수 추가 CREATE_QUANTITY, IMAGE_SIZE
     * 2025/09/22 오전 11:27    minki-jeon         번역할 텍스트 -> 프롬프트 형식
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
        String prompt = "`" + inputText + "` " + Constants.PROMPT_TRANSLATE_INPUT;
        String text = Constants.PROMPT_CREATE_IMAGE_HEADER_DETAIL
                + callText2TextAgent(prompt, null)
                + Constants.PROMPT_CREATE_IMAGE_OPTIMIZATION_DETAIL;

        String model = Constants.MODEL_OPENAI_GPT_IMAGE_1;
        int n = Constants.CREATE_QUANTITY;
        String size = Constants.IMAGE_SIZE;

        // OpenAI Image Model API 호출
        String apiUrl = Constants.API_URL_OPENAI_RESPONSES_IMAGES;

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "prompt", text,
                "n", n,
                "size", size
        );

        // Call API openAI
        ResponseEntity<Map> response = callApi.callOpenAi(apiUrl, requestBody);

        // Base64 디코딩
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
        /* TODO output_format */
        String output_format = (String) response.getBody().get("output_format");
        String base64Image = (String) data.get(0).get("b64_json");
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // 파일명 생성
        String[] fileNameArr = utils.createFileName();
        String fileName = fileNameArr[0];
        String uuid = fileNameArr[1];
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String date = today.format(formatter);
        String filePathName = date + "/" + fileName;
        String objectKey = objectUrlImage + filePathName;
        // s3 업로드
        utils.s3UploadByteImage(imageBytes, objectKey);

        // db save
        Image image = new Image();
        image.setUuid(uuid);
        image.setS3Key(objectKey);
        String s3url = s3UrlPrefix + objectUrlImage + filePathName;
        image.setS3Url(s3url);
        imageRepository.save(image);

        return s3url;
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
     * 2025/09/22 오전 11:25    minki-jeon         parameter model 추가, text -> prompt
     *
     * </pre>
     *
     * @param prompt 사용자로부터 입력받은 텍스트를 결합한 프롬프트
     * @param model  호출 모델
     * @return 응답받은 텍스트
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    private String callText2TextAgent(String prompt, String model) {
        model = (model == null) ? Constants.MODEL_OPENAI_GPT_4O_MINI : model;

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

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/16 오후 3:07
     * description    : 생성 예상 시간 반환
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/16 오후 3:07     minki-jeon         최초 생성.
     *
     * </pre>
     *
     * @param modelName
     * @return waitingTime
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    public Integer getWaitingTime(String modelName) {
        return apiLogRepository.findLastProcMs(modelName);
    }

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/22 오전 11:03
     * description    : 4컷 동화 이미지 생성
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/22 오전 11:03     minki-jeon         최초 생성.
     * 2025/09/22 오전 14:13     minki-jeon         문장 생성, 번역 프롬프트 분리 + 생성된 문장 전달 (Map타입 반환).
     *
     * </pre>
     *
     * @param request
     * @return 생성된 이미지 경로 4개, 생성된 문장 4개
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    public Map<String, Object> createFourPanelImage(FourPanelRequestDto request) {
        Map<String, Object> result = new HashMap<String, Object>();
        String startScene = request.getStartScene();
        String endScene = request.getEndScene();
        // 입력 텍스트들을 문장 생성 동화 이미지에 맞는 4개 문장으로 생성
        String createTextPrompt = Constants.PROMPT_CREATE_FOUR_PANEL_TEXT
                + " `" + startScene + "`, "
                + " `" + endScene + "`";
        String createTexts = callText2TextAgent(createTextPrompt, Constants.MODEL_OPENAI_GPT_4O);
        String[] createTextArr = createTexts.split("\\|\\|\\|");
        result.put("createTexts", createTextArr);

        // 영문으로 번역
        String translatePrompt = "`" + createTexts + "` " + Constants.PROMPT_TRANSLATE_INPUT;
        String translateTexts = callText2TextAgent(translatePrompt, null);
        String[] translateTextArr = translateTexts.split("\\|\\|\\|");

        String[] paths = new String[translateTextArr.length];
        for (int i = 0; i < translateTextArr.length; i++) {
            String text = Constants.PROMPT_CREATE_IMAGE_HEADER_DETAIL
                    + translateTextArr[i]
                    + Constants.PROMPT_CREATE_IMAGE_OPTIMIZATION_DETAIL;

            String model = Constants.MODEL_OPENAI_GPT_IMAGE_1;
            int n = Constants.CREATE_QUANTITY;
            String size = Constants.IMAGE_SIZE;

            // OpenAI Image Model API 호출
            String apiUrl = Constants.API_URL_OPENAI_RESPONSES_IMAGES;

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "prompt", text,
                    "n", n,
                    "size", size
            );

            // Call API openAI
            ResponseEntity<Map> response = callApi.callOpenAi(apiUrl, requestBody);

            // Base64 디코딩
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
            /* TODO output_format */
            String output_format = (String) response.getBody().get("output_format");
            String base64Image = (String) data.get(0).get("b64_json");
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // 파일명 생성
            String[] fileNameArr = utils.createFileName();
            String fileName = fileNameArr[0];
            String uuid = fileNameArr[1];
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String date = today.format(formatter);
            String filePathName = date + "/" + fileName;
            String objectKey = objectUrlImage + filePathName;
            // s3 업로드
            utils.s3UploadByteImage(imageBytes, objectKey);

            // db save
            Image image = new Image();
            image.setUuid(uuid);
            image.setS3Key(objectKey);
            String s3url = s3UrlPrefix + objectUrlImage + filePathName;
            image.setS3Url(s3url);
            imageRepository.save(image);

            paths[i] = s3url;
        }

        result.put("imagePaths", paths);

        return result;
    }
}
