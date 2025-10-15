# 📚 DreamTales - AI 동화책 생성기

<div align="center">
  <img src="https://img.shields.io/badge/React-19.1-61DAFB?style=for-the-badge&logo=react&logoColor=white" alt="React">
  <img src="https://img.shields.io/badge/Spring_Boot-3.5.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java">

[//]: # (  <img src="https://img.shields.io/badge/Vite-7.1-646CFF?style=for-the-badge&logo=vite&logoColor=white" alt="Vite">)
  <img src="https://img.shields.io/badge/OpenAI-GPT--IMAGE--1-412991?style=for-the-badge&logo=openai&logoColor=white" alt="OpenAI">
  <img src="https://img.shields.io/badge/OpenAI-GPT--4o-412991?style=for-the-badge&logo=openai&logoColor=white" alt="OpenAI">
  <img src="https://img.shields.io/badge/OpenAI-GPT--4o--mini-412991?style=for-the-badge&logo=openai&logoColor=white" alt="OpenAI">
</div>

<div align="center">
  <h3>✨ 상상 속 이야기를 마법같은 그림으로 만들어보세요 ✨</h3>
  <p>한 문장이 아름다운 동화 일러스트가 됩니다</p>
</div>

---

## 🎨 프로젝트 소개

**DreamTales**는 OpenAI의 GPT-4o와 GPT-IMAGE-1을 활용하여 사용자의 상상력을 동화책 일러스트로 변환하는 AI 기반 웹 애플리케이션입니다. 텍스트 입력만으로 수채화 스타일의 아름다운
동화책
이미지를 생성할 수 있으며, 4컷 스토리 생성 기능을 통해 완성도 높은 이야기를 만들 수 있습니다.

### 🌟 주요 특징

- **한국어 입력 지원**: 한국어로 입력해도 자동으로 영어로 번역하여 최적의 이미지 생성
- **수채화 스타일**: 부드럽고 따뜻한 동화책 일러스트 스타일 적용
- **실시간 진행 상황**: 예상 대기 시간과 진행 시간을 실시간으로 표시
- **클라우드 저장**: AWS S3를 통한 안전한 이미지 저장 및 관리
- **반응형 디자인**: 모바일, 태블릿, 데스크톱 모든 환경에서 최적화된 UI

## 🚀 주요 기능

### 1️⃣ 한 컷 동화 만들기

- 한 문장으로 완성되는 동화 일러스트 생성
- 텍스트 설명을 입력하면 AI가 동화책 스타일의 일러스트로 변환
- 최대 200자까지 입력 가능

### 2️⃣ 네 컷 동화 만들기

- 시작과 끝 장면만 입력하면 중간 스토리를 AI가 자동 생성
- 4개의 연속된 장면으로 완성되는 동화책 제작
- 각 장면은 책 넘기기 애니메이션으로 감상 가능
- 생성된 스토리 텍스트도 함께 제공

### 3️⃣ 실시간 생성 모니터링

- 이전 생성 기록 기반 예상 대기 시간 표시
- 실시간 진행 시간 카운터
- 진행률 표시바 제공

## 🛠️ 기술 스택

### Frontend

- **Framework**: React 19.1.1
- **Build Tool**: Vite 7.1.2
- **UI Framework**: React Bootstrap 2.10
- **HTTP Client**: Axios 1.11.0
- **Icons**: Lucide React, React Icons
- **Styling**: CSS3 with custom animations
- **Notifications**: React Toastify

### Backend

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: MariaDB
- **ORM**: Spring Data JPA
- **Cloud Storage**: AWS S3
- **AI Integration**:
    - OpenAI GPT-4o-mini (텍스트 번역)
    - OpenAI GPT-4o (스토리 생성)
    - OpenAI GPT-Image-1 (이미지 생성)
- **Build Tool**: Gradle 8.14

## 📁 프로젝트 구조

```
dream-tales/
├── frontend/                      # React + Vite 프론트엔드
│   ├── src/
│   │   ├── MainView.jsx         # 메인 컴포넌트 (동화 생성 UI)
│   │   ├── App.jsx              # 루트 컴포넌트
│   │   ├── main.jsx             # 애플리케이션 엔트리 포인트
│   │   ├── css/
│   │   │   ├── MainView.css    # 메인 뷰 스타일 (애니메이션 포함)
│   │   │   └── index.css       # 글로벌 스타일
│   │   └── assets/
│   ├── public/
│   │   ├── favicon.svg
│   │   └── thumbnail.png
│   ├── package.json
│   ├── vite.config.js           # Vite 설정 (프록시 포함)
│   └── index.html
│
└── backend/                       # Spring Boot 백엔드
    ├── src/main/java/com/example/backend/
    │   ├── config/
    │   │   └── AppConfiguration.java    # Security, S3 설정
    │   ├── feature/
    │   │   ├── common/
    │   │   │   ├── CallApi.java        # OpenAI API 호출
    │   │   │   ├── Constants.java      # 상수 관리
    │   │   │   ├── Utils.java          # S3 업로드 유틸
    │   │   │   ├── entity/             # JPA 엔티티
    │   │   │   └── repository/         # JPA 레포지토리
    │   │   └── main/
    │   │       ├── controller/
    │   │       │   └── MainController.java
    │   │       ├── dto/
    │   │       │   ├── ImageRequestDto.java
    │   │       │   └── FourPanelRequestDto.java
    │   │       └── service/
    │   │           └── MainService.java
    │   └── BackendApplication.java
    ├── src/main/resources/
    │   └── application.properties
    └── build.gradle
```

## 🚀 시작하기

### 필수 요구사항

- **Java 21** 이상
- **Node.js 18** 이상
- **MariaDB**
- **AWS 계정** (S3 사용)
- **OpenAI API Key**

### 환경 설정

#### 1. 프로젝트 클론

```bash
git clone https://github.com/minki-jeon/dream-tales.git
cd dream-tales
```

#### 2. Backend 설정

`backend/src/main/resources/secret/custom.properties` 파일 생성:

```properties
# Database Configuration
spring.datasource.url=jdbc:mariadb://localhost:3306/prj5
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
# OpenAI API Configuration
openai.api.key=your_openai_api_key
# AWS S3 Configuration
aws.access.key=your_aws_access_key
aws.secret.key=your_aws_secret_key
aws.s3.bucket.name=your_bucket_name
aws.s3.url.prefix=https://your_bucket_name.s3.ap-northeast-2.amazonaws.com/
object.url.image=images/
```

#### 3. 데이터베이스 테이블 생성

```sql
CREATE SCHEMA IF NOT EXISTS prj5;
USE prj5;

-- API 로그 테이블
CREATE TABLE api_log
(
    seq         BIGINT AUTO_INCREMENT PRIMARY KEY,
    url         VARCHAR(500) NOT NULL,
    model_nm    VARCHAR(100) NOT NULL,
    model_ver   VARCHAR(50),
    prompt      TEXT         NOT NULL,
    req_body    TEXT,
    req_hdr     TEXT,
    res_body    TEXT,
    res_hdr     TEXT,
    stat_cd     INT,
    err_msg     TEXT,
    req_dttm    DATETIME     NOT NULL,
    res_dttm    DATETIME     NOT NULL,
    latency_ms  INT,
    proc_ms     INT,
    usage_token INT,
    usr_id      VARCHAR(100)
);

-- 이미지 정보 테이블
CREATE TABLE image
(
    seq        BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid       VARCHAR(36)   NOT NULL,
    s3_key     VARCHAR(512)  NOT NULL,
    s3_url     VARCHAR(1024) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    meta       TEXT
);
```

#### 4. Frontend 설정 (필요시)

개발 환경에서 백엔드 프록시는 `vite.config.js`에 이미 설정되어 있습니다:

```javascript
proxy: {
  "/api"
:
  {
    target: "http://localhost:8080",
  }
,
}
```

### 실행 방법

#### Backend 실행

```bash
cd backend
./gradlew clean build
./gradlew bootRun
```

서버는 `http://localhost:8080`에서 실행됩니다.

#### Frontend 실행

```bash
cd frontend
npm install
npm run dev --host
```

개발 서버는 `http://localhost:5173`에서 실행됩니다.

### 프로덕션 배포

#### Backend 빌드

```bash
cd backend
./gradlew clean build
java -jar build/libs/backend-0.0.1-SNAPSHOT.jar
```

#### Frontend 빌드

```bash
cd frontend
npm run build
# dist 폴더의 내용을 웹 서버에 배포
```

## 📝 API 명세

### 1. 단일 이미지 생성

- **Endpoint**: `POST /api/create/images`
- **Request Body**:
  ```json
  {
    "text": "파란 하늘 아래 노란 꽃이 피어있는 정원"
  }
  ```
- **Response**:
  ```json
  {
    "image_path": "https://bucket.s3.amazonaws.com/images/2025/09/25/uuid_timestamp.png"
  }
  ```

### 2. 4컷 동화 생성

- **Endpoint**: `POST /api/create/four-panel-story`
- **Request Body**:
  ```json
  {
    "startScene": "작은 토끼가 숲에서 길을 잃었어요",
    "endScene": "토끼는 엄마와 다시 만나 행복했어요"
  }
  ```
- **Response**:
  ```json
  {
    "image_paths": [
      "https://bucket.s3.amazonaws.com/images/2025/09/25/uuid1.png",
      "https://bucket.s3.amazonaws.com/images/2025/09/25/uuid2.png",
      "https://bucket.s3.amazonaws.com/images/2025/09/25/uuid3.png",
      "https://bucket.s3.amazonaws.com/images/2025/09/25/uuid4.png"
    ],
    "create_texts": [
      "작은 토끼가 숲에서 길을 잃었어요",
      "토끼는 나비를 따라가며 길을 찾기 시작했어요",
      "친절한 다람쥐가 토끼에게 집으로 가는 길을 알려주었어요",
      "토끼는 엄마와 다시 만나 행복했어요"
    ]
  }
  ```

### 3. 예상 대기 시간 조회

- **Endpoint**: `GET /api/create/waiting_time?model={modelName}`
- **Response**:
  ```json
  {
    "waitingTime": 15
  }
  ```

## 🎨 UI/UX 특징

### 애니메이션 효과

- **초기 로딩**: 마법의 포털 애니메이션
- **플로팅 요소**: 별, 하트, 책 아이콘이 떠다니는 효과
- **타이핑 효과**: 생성된 텍스트가 타이핑되는 듯한 효과
- **책 넘기기**: 4컷 동화에서 실제 책을 넘기는 듯한 3D 애니메이션

### 반응형 디자인

- 모바일, 태블릿, 데스크톱 모든 환경 지원
- 화면 크기에 따른 레이아웃 자동 조정
- 터치 친화적인 인터페이스

## 🔧 주요 기술 구현

### 텍스트-이미지 변환 프로세스

1. 사용자가 한국어로 장면 설명 입력
2. GPT-4o-mini를 통한 영어 번역
3. 동화책 스타일 프롬프트 최적화
4. GPT-Image-1 모델로 이미지 생성
5. Base64 디코딩 및 S3 업로드
6. 생성된 이미지 URL 반환

### 로깅 시스템

- 모든 API 호출 내역 데이터베이스 기록
- 요청/응답 데이터, 처리 시간, 토큰 사용량 추적
- 성능 모니터링 및 비용 관리

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE.md) 파일을 참조하세요.

## 📢 OpenAI 모델 사용 안내

이 프로젝트는 OpenAI의 GPT 계열 모델(GPT-4o, GPT-4o-mini)과 GPT-Image-1 모델을 활용합니다.  
모델 사용은 [OpenAI Terms of Service](https://openai.com/policies/terms-of-use)
와 [Usage Policies](https://platform.openai.com/docs/usage-policies)를 준수합니다.

## 👥 개발자

- **Full Stack Development**: [@minki-jeon](https://github.com/minki-jeon)

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 [Issues](https://github.com/minki-jeon/dream-tales/issues) 탭을 통해 문의해주세요.

---
**⚠️ 주의사항**: 본 프로젝트는 포트폴리오 목적으로 제작되었으며, 상업적 용도로 사용되지 않습니다.
