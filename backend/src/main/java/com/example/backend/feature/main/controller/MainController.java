package com.example.backend.feature.main.controller;

import com.example.backend.feature.main.dto.ImageRequestDto;
import com.example.backend.feature.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * description    : dall-e-2 모델 이미지 생성
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
     * </pre>
     *
     * @param request Dall-e-2 API 요청 Parameter DTO
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "이미지 생성 실패", "detail", e.getMessage()));
        }
    }


}
