package com.example.backend.feature.common;

/**
 * <pre>
 * packageName    : com.example.backend.feature.common
 * fileName       : Constants
 * author         : minki-jeon
 * date           : 2025-09-04 (목)
 * description    : 상수 관리
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-04 (목)        minki-jeon       최초 생성
 * 2025-09-05 (금)        minki-jeon       Rename Constant to Constants
 * 2025-09-05 (금)        minki-jeon       Add ApiUrl, Model
 * </pre>
 */
public final class Constants {

    // 인스턴스화 방지
    private Constants() {
    }


    /**
     * API URL
     */

    public static final String API_URL_OPENAI_RESPONSES = "https://api.openai.com/v1/responses";
    public static final String API_URL_OPENAI_RESPONSES_IMAGES = "https://api.openai.com/v1/images/generations";


    /**
     * MODEL
     */
    public static final String MODEL_OPENAI_DALL_E_2 = "dall-e-2";
    public static final String MODEL_OPENAI_GPT_4O_MINI = "gpt-4o-mini-2024-07-18";

    /**
     * PROMPT
     */

}
