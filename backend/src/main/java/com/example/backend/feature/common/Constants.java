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
 * 2025-09-09 (화)        minki-jeon       Add Optimization Image Prompt, Add Image Model
 * 2025-09-14 (일)        minki-jeon       Add option
 * </pre>
 */
public final class Constants {

    // 인스턴스화 방지
    private Constants() {
    }

    /**
     * ETC
     */
    public static final int CREATE_QUANTITY = 1;
    public static final String IMAGE_SIZE = "1024x1024";


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
    public static final String MODEL_OPENAI_GPT_4O = "chatgpt-4o-latest";
    public static final String MODEL_OPENAI_GPT_IMAGE_1 = "gpt-image-1";


    /**
     * PROMPT
     */
    public static final String PROMPT_TRANSLATE_INPUT = "문장만 영어로 번역하라. 번역한 문장 외에는 응답하지말라.";
    public static final String PROMPT_TRANSLATE_INPUT_2 = """
            gpt-image-1로 4장면 동화를 만든다.
            첫·마지막 장면 문장을 줄 테니, 이를 이어 2·3번째 장면을 자연스럽게 생성하라.
            4문장을 번역하고 파이프 3개(|||)로 구분해라.
            번역한 문장 외에는 응답하지말라.
            """;
    public static final String PROMPT_CREATE_IMAGE_HEADER_SHORT = "Storybook illustration of: \"";
    public static final String PROMPT_CREATE_IMAGE_HEADER_DETAIL = "Create a storybook-style illustration base on the following description: \"";
    public static final String PROMPT_CREATE_IMAGE_OPTIMIZATION_SHORT = """
            \"
            Style: watercolor, pastel colors, child-friendly, soft lines
            Mood: warm, whimsical, imaginative
            High quality, full color
            """;
    public static final String PROMPT_CREATE_IMAGE_OPTIMIZATION_MIDDLE = """
            \"
            Requirements: 
              - Style: watercolor, pastel colors, child-friendly, soft lines, hand-drawn illustration
              - Atmosphere: warm, whimsical, and imaginative, suitable for a children's picture book
              - Resolution: high quality, detailed, full color
            """;
    public static final String PROMPT_CREATE_IMAGE_OPTIMIZATION_DETAIL = """
            \"
            Requirements: 
              - Style: watercolor, pastel colors, child-friendly, soft lines, hand-drawn illustration
              - Atmosphere: warm, whimsical, and imaginative, suitable for a children's picture book
              - Characters: consistent and expressive, with friendly and gentle facial expressions
              - Background: simple but colorful, enhancing the storytelling without overwhelming
              - Resolution: high quality, detailed, full color
            
              Output format: single illustration
            """;


}
