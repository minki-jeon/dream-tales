package com.example.backend.feature.common;

import com.example.backend.feature.common.entity.ApiLog;
import com.example.backend.feature.common.repository.ApiLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
 * 2025-09-08 (월)        minki-jeon       ApiLog Save
 * </pre>
 */
@Component
public class CallApi {

    @Value("${openai.api.key}")
    private String openaiApiKey;
    private final ApiLogRepository apiLogRepository;

    public CallApi(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/04 오후 4:49
     * description    : OpenAI 호출
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/04 오후 4:49     minki-jeon         최초 생성.
     * 2025/09/04 오후 4:49     minki-jeon         apiKey null 오류조치
     * 2025/09/08 오후 5:42     minki-jeon         Save ApiLog Data(Req, Res)
     * 2025/09/10 오후 12:15    minki-jeon         테이블 api_log 필드 prompt 추가
     * 2025/09/11 오후 10:11    minki-jeon         테이블 api_log - Error 기록
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
    public ResponseEntity<Map> callOpenAi(String apiUrl, Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        // request log
        LocalDateTime requestTime = LocalDateTime.now();
        ApiLog apiLog = new ApiLog();
        apiLog.setUrl(apiUrl);
        apiLog.setModelNm(requestBody.get("model").toString());
        Object prompt = requestBody.get("prompt") == null ? requestBody.get("input") : requestBody.get("prompt");
        apiLog.setPrompt(prompt.toString());
        apiLog.setReqBody(requestBody.toString());
        apiLog.setReqHdr(headers.toString());
        apiLog.setReqDttm(requestTime);

        //TODO Create ResponseDTO
        ResponseEntity<Map> response = null;
        try {
            response = restTemplate.postForEntity(apiUrl, entity, Map.class);
        } catch (HttpStatusCodeException e) {
            procException(e, apiLog);
        }

        // response log
        // TODO Response Log 저장 로직은 분리 (Frontend의 finally에서 별도 호출)
        LocalDateTime responseTime = LocalDateTime.now();
        apiLog.setStatCd(response.getStatusCode().value());
        apiLog.setResBody(response.getBody().toString());
        apiLog.setResHdr(response.getHeaders().toString());
        apiLog.setResDttm(responseTime);
        apiLog.setLatencyMs((int) ChronoUnit.NANOS.between(requestTime, responseTime));

        // apiLog.setStatCd(response.getBody().get("status"));
        // apiLog.setErrMsg(response.getBody().get("error"));

        apiLogRepository.save(apiLog);

        return response;
    }

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/11 오후 10:11
     * description    : Error 기록
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/11 오후 10:11     minki-jeon         최초 생성.
     *
     * </pre>
     *
     * @param e
     * @param apiLog
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    public void procException(HttpStatusCodeException e, ApiLog apiLog) {
        apiLog.setStatCd(e.getStatusCode().value());
        apiLog.setModelVer(e.getResponseHeaders().get("openai-version").toString());
        apiLog.setResBody(e.getResponseBodyAsString());
        apiLog.setResHdr(e.getResponseHeaders().toString());
        apiLog.setErrMsg(e.getMessage());
        LocalDateTime responseTime = LocalDateTime.now();
        apiLog.setResDttm(responseTime);
        apiLogRepository.save(apiLog);
    }
}
