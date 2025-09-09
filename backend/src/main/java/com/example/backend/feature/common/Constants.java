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
    public static final String MODEL_OPENAI_GPT_IMAGE_1 = "GPT-image-1";

    /**
     * PROMPT
     */
    public static final String PROMPT_TRANSLATE_INPUT = """
            내가 제공한 문장을 영어로 번역해서 제공해주세요. 
            번역한 문장 외에는 응답하지마세요.
            """;
    public static final String PROMPT_CREATE_IMAGE_HEADER_SHORT = "Storybook illustration of: \"";
    public static final String PROMPT_CREATE_IMAGE_HEADER_DETAIL = "Create a storybook-style illustration base on the following description: \"";
    public static final String PROMPT_CREATE_IMAGE_OPTIMIZATION_SHORT = """
            \"
            Style: watercolor, pastel colors, child-friendly, soft lines\n
            Mood: warm, whimsical, imaginative\n
            High quality, full color\n
            """;
    public static final String PROMPT_CREATE_IMAGE_OPTIMIZATION_MIDDLE = """
            \"
            Requirements: \n
              - Style: watercolor, pastel colors, child-friendly, soft lines, hand-drawn illustration\n
              - Atmosphere: warm, whimsical, and imaginative, suitable for a children's picture book\n
              - Resolution: high quality, detailed, full color\n
            """;
    public static final String PROMPT_CREATE_IMAGE_OPTIMIZATION_DETAIL = """
            \"
            Requirements: \n
              - Style: watercolor, pastel colors, child-friendly, soft lines, hand-drawn illustration\n
              - Atmosphere: warm, whimsical, and imaginative, suitable for a children's picture book\n
              - Characters: consistent and expressive, with friendly and gentle facial expressions\n
              - Background: simple but colorful, enhancing the storytelling without overwhelming\n
              - Resolution: high quality, detailed, full color\n
                \n
              Output format: single illustration\n
            """;


}
