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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final CallApiService callApiService;

    @PostMapping("/create/dalle2")
    public ResponseEntity<Map<String, Object>> generateImage(@RequestBody ImageRequestDto request) {
        /* TODO
        (1) Service로 구현 소스 이동
		(2) 입력 텍스트(프롬프트) 영문 번역
		(3) 기본 프롬프트 설정 (ex. 동화/삽화 이미지에 어울리는)
		(4) API 호출응답에 소요되는 시간 관리, 평균 시간 화면에 출력
		(5) Error 처리 (오류코드 별 메시지 출력)
		(6) Token 사용량 체크/제한 처리
		(7) 이미지 재생성 및 편집 등 기능 추가
		(8) 다른 모델 및 AI API 추가 반영
         */
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
