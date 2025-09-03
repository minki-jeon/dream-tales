package com.example.backend.feature.main.controller;

import com.example.backend.feature.main.dto.ImageRequestDto;
import com.example.backend.feature.main.service.CallApiService;
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

    private final CallApiService callApiService;

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
     * </pre>
     *
     * @param request Dall-e-2 API 요청 Parameter DTO
     * @return axios promise
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    @PostMapping("/create/dalle2")
    public ResponseEntity<Map<String, Object>> generateImage(@RequestBody ImageRequestDto request) {
        try {
            String imageUrl = callApiService.createImageOnDallE2(request);

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
