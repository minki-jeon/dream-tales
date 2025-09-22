package com.example.backend.feature.main.dto;

import lombok.Data;

/**
 * <pre>
 * packageName    : com.example.backend.feature.main.dto
 * fileName       : FourPanelRequestDto
 * author         : minki-jeon
 * date           : 2025-09-22 (월)
 * description    : 4컷 이미지 생성 API 요청 Parameter DTO
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-22 (월)        minki-jeon       최초 생성
 * </pre>
 */
@Data
public class FourPanelRequestDto {
    String startScene;
    String endScene;
}
