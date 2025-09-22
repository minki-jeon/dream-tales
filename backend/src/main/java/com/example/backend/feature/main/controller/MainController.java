package com.example.backend.feature.main.controller;

import com.example.backend.feature.main.dto.FourPanelRequestDto;
import com.example.backend.feature.main.dto.ImageRequestDto;
import com.example.backend.feature.main.service.MainService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Map;

/**
 * <pre>
 * packageName    : com.example.backend.feature.main.controller
 * fileName       : MainController
 * author         : minki-jeon
 * date           : 2025-08-29 (금)
 * description    : 메인 화면에서 실행되는 기능
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-08-29 (금)        minki-jeon       최초 생성
 * </pre>
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/08/29 오후 2:51
     * description    : 단일 이미지 생성
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/08/29 오후 2:51     minki-jeon         최초 생성.
     * 2025/09/01 오후 6:14     minki-jeon         To Service
     * 2025/09/04 오후 4:45     minki-jeon         Rename callApiService
     * 2025/09/08 오후 5:40     minki-jeon         e.getMessage()
     * 2025/09/09 오후 12:33    minki-jeon         postMapping url 변경
     * 2025/09/09 오후 12:36    minki-jeon         createImageOnDallE2 메소드명 변경
     * 2025/09/11 오후 12:09    minki-jeon         불필요한 소스 삭제
     * 2025/09/17 오전 10:50    minki-jeon         오류 view 전달
     * </pre>
     *
     * @param request 이미지 생성 API 요청 Parameter DTO
     * @return axios promise
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    @PostMapping("/create/images")
    public ResponseEntity<Map<String, Object>> generateImage(@RequestBody ImageRequestDto request) {
        try {
            String imageUrl = mainService.createImage(request);
            return ResponseEntity.ok(Map.of("image_path", imageUrl));
        } catch (HttpStatusCodeException e) {
            try {
                // TODO Exception 처리 함수 생성
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(e.getResponseBodyAsString());
                String errMsg = root.path("error").path("message").asText();
                return ResponseEntity.status(e.getStatusCode())
                        .body(Map.of("error", "이미지 생성 실패", "detail", errMsg));
            } catch (Exception ex) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "예외 처리 중 오류 발생", "detail", ex.getMessage()));
            }

        }
    }

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/22 오전 11:00
     * description    : 4컷 동화 이미지 생성
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/22 오전 11:00     minki-jeon         최초 생성.
     * 2025/09/22 오전 14:13     minki-jeon         생성된 문장 4개 view 전달.
     *
     * </pre>
     *
     * @param request 이미지 4개 생성 API 요청 Parameter DTO
     * @return axios promise
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    @PostMapping("/create/four-panel-story")
    public ResponseEntity<Map<String, Object>> generateFourPanelImage(@RequestBody FourPanelRequestDto request) {
        try {
            Map<String, Object> result = mainService.createFourPanelImage(request);
            return ResponseEntity.ok(Map.of("image_paths", result.get("imagePaths"),
                    "create_texts", result.get("createTexts")));
        } catch (HttpStatusCodeException e) {
            try {
                // TODO Exception 처리 함수 생성
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(e.getResponseBodyAsString());
                String errMsg = root.path("error").path("message").asText();
                return ResponseEntity.status(e.getStatusCode())
                        .body(Map.of("error", "이미지 생성 실패", "detail", errMsg));
            } catch (Exception ex) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "예외 처리 중 오류 발생", "detail", ex.getMessage()));
            }

        }
    }


    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/16 오후 3:01
     * description    : 생성 예상 시간 반환
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/16 오후 3:01     minki-jeon         최초 생성.
     *
     * </pre>
     *
     * @param model
     * @return axios promise
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    @GetMapping("/create/waiting_time")
    public ResponseEntity<Map<String, Object>> waitingTime(@RequestParam String model) {
        int waitingTime = (int) (mainService.getWaitingTime(model) * 1.275) / 1000;
        return ResponseEntity.ok(Map.of("waitingTime", waitingTime));
    }

}
