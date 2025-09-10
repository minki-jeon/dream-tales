# CALL API Request/Response
CREATE TABLE api_log
(
    seq        BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유번호
    url        VARCHAR(500) NOT NULL,             -- API 호출 URL
    model_nm   VARCHAR(100) NOT NULL,             -- 모델 이름
    model_ver  VARCHAR(50),                       -- 모델 버전
    prompt     TEXT         NOT NULL,             -- 요청 프롬프트
    req_body   MEDIUMTEXT,                        -- 요청 Body (JSON/Text)
    req_hdr    MEDIUMTEXT,                        -- 요청 Header
    res_body   MEDIUMTEXT,                        -- 응답 Body (JSON/Text)
    res_hdr    MEDIUMTEXT,                        -- 응답 Header
    stat_cd    INT,                               -- 응답 HTTP Status Code
    err_msg    TEXT,                              -- 오류 메시지
    req_dttm   DATETIME     NOT NULL,             -- 요청 시각
    res_dttm   DATETIME,                          -- 응답 시각
    latency_ms INT,                               -- 응답 소요 시간 (ms)
    usr_id     VARCHAR(100)                       -- 호출 사용자/세션 ID
);

# image
CREATE TABLE image
(
    seq        BIGINT AUTO_INCREMENT PRIMARY KEY,    -- 고유번호
    uuid       CHAR(36)      NOT NULL,               -- UUID (고유 식별자)
    s3_key     VARCHAR(512)  NOT NULL,               -- S3 객체 키
    s3_url     VARCHAR(1024) NOT NULL,               -- S3 접근 URL (or Pre-signed URL)
    created_at DATETIME      NOT NULL DEFAULT NOW(), -- 생성 시각
    meta       JSON                                  -- 부가 정보 (프롬프트, 모델, 스타일, 옵션 등)
);


