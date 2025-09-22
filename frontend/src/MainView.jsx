import React, { useState, useRef, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Form,
  Spinner,
} from "react-bootstrap";
import {
  Sparkles,
  Wand2,
  Book,
  Stars,
  Heart,
  Smile,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";
import "./css/MainView.css";
import axios from "axios";

export function MainView() {
  const [storyType, setStoryType] = useState("single"); // "single" 또는 "four-panel"
  const [inputText, setInputText] = useState("");
  const [startSceneText, setStartSceneText] = useState("");
  const [endSceneText, setEndSceneText] = useState("");
  const [isGenerating, setIsGenerating] = useState(false);
  const [generatedImage, setGeneratedImage] = useState(null);
  const [generatedImages, setGeneratedImages] = useState([]); // 4컷용
  const [generatedTexts, setGeneratedTexts] = useState([]); // 4컷 생성 문장
  const [displayText, setDisplayText] = useState("");
  const [isTyping, setIsTyping] = useState(false);
  const [isInitialLoading, setIsInitialLoading] = useState(true);
  const [waitingTime, setWaitingTime] = useState(null); // 예상 시간 (초)
  const [runningTime, setRunningTime] = useState(0); // 진행 시간 (초)
  const [currentPage, setCurrentPage] = useState(0); // 책 페이지 인덱스
  const resultRef = useRef(null);
  const timerRef = useRef(null); // 타이머 참조

  // 초기 로딩 애니메이션 제어
  useEffect(() => {
    const timer = setTimeout(() => {
      setIsInitialLoading(false);
    }, 3000); // 3초 후 메인 화면 표시

    return () => clearTimeout(timer);
  }, []);

  // 타이머 시작 함수
  const startTimer = () => {
    setRunningTime(0);
    timerRef.current = setInterval(() => {
      setRunningTime((prev) => prev + 1);
    }, 1000);
  };

  // 타이머 중지 함수
  const stopTimer = () => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
      timerRef.current = null;
    }
  };

  // 컴포넌트 언마운트 시 타이머 정리
  useEffect(() => {
    return () => stopTimer();
  }, []);

  // 타이핑 애니메이션 효과
  const typeWriterEffect = (text) => {
    setIsTyping(true);
    setDisplayText("");
    let i = -1; // 기존 0은 set 렌더링 타이밍에 의해 첫번째 text.charAt은 setDisplayText가 무시되어 -1로 변경
    const timer = setInterval(() => {
      if (i < text.length) {
        setDisplayText((prev) => prev + text.charAt(i));
        i++;
      } else {
        clearInterval(timer);
        setIsTyping(false);
      }
    }, 50);
  };

  const getWatingTime = async (n) => {
    try {
      const result = await axios.get("/api/create/waiting_time", {
        params: { model: "gpt-image-1" },
      });
      console.log(result.data);
      setWaitingTime(result.data.waitingTime * n + 5);
    } catch (err) {
      console.log("[Error] watingTime() : ", err);
    }
  };

  // 단일 이미지 생성 (backend에서 api 연동)
  const handleCreateSingleImage = async () => {
    if (!inputText.trim()) return;
    setIsGenerating(true);

    // 출력 예상 시간
    getWatingTime(1);
    startTimer();

    try {
      const response = await axios.post("/api/create/images", {
        text: inputText,
      });
      console.log(response.data);

      setTimeout(() => {
        setGeneratedImage(response.data.image_path);
        setIsGenerating(false);
        stopTimer();
        scrollToResult();
        setTimeout(() => typeWriterEffect(inputText), 500);
      }, 3000);
    } catch (err) {
      stopTimer();
      handleApiError(err);
    }
  };

  // 4컷 동화 생성
  const handleCreateFourPanelStory = async () => {
    if (!startSceneText.trim() || !endSceneText.trim()) return;
    setIsGenerating(true);
    getWatingTime(4);
    startTimer();

    try {
      const response = await axios.post("/api/create/four-panel-story", {
        startScene: startSceneText,
        endScene: endSceneText,
      });
      console.log(response.data);

      setTimeout(() => {
        setGeneratedImages(response.data.image_paths); // 4개의 이미지 URL 배열
        setGeneratedTexts(response.data.create_texts);
        setCurrentPage(0);
        setIsGenerating(false);
        stopTimer();
        scrollToResult();
        setTimeout(
          () => typeWriterEffect(`${startSceneText} ➡️ ${endSceneText}`),
          500,
        );
      }, 3000);
    } catch (err) {
      stopTimer();
      handleApiError(err);
    }
  };

  const handleApiError = (err) => {
    console.log("[Error] API call failed: ", err);
    setIsGenerating(false);
    stopTimer(); // 오류 시 타이머 중지
    if (
      confirm(
        "오류가 발생되었습니다. 새로 고침하려면 확인 버튼을 눌러주세요. \n 오류 : " +
          (err.response?.data?.detail || err.message),
      )
    ) {
      location.reload();
    }
  };

  const scrollToResult = () => {
    setTimeout(() => {
      resultRef.current?.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    }, 100);
  };

  // 책 페이지 넘기기
  const nextPage = () => {
    if (currentPage < generatedImages.length) {
      setCurrentPage(currentPage + 1);
    }
  };

  const prevPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  // 초기화 함수
  const resetAll = () => {
    setGeneratedImage(null);
    setGeneratedImages([]);
    setGeneratedTexts([]);
    setDisplayText("");
    setInputText("");
    setStartSceneText("");
    setEndSceneText("");
    setCurrentPage(0);
    setRunningTime(0); // 진행 시간 초기화
    setWaitingTime(null); // 예상 시간 초기화
    stopTimer(); // 타이머 중지
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  // 플로팅 아이콘들
  const FloatingIcon = ({ icon: Icon, delay, size = "floating-icon" }) => (
    <div
      className={`floating-icon ${size}`}
      style={{
        animationDelay: `${delay}s`,
        left: `${Math.random() * 90}%`,
        top: `${Math.random() * 80}%`,
      }}
    >
      <Icon />
    </div>
  );

  return (
    <>
      {/* 초기 로딩 화면 */}
      {isInitialLoading && (
        <div className="loading-screen">
          <div className="forest-portal">
            <div className="portal-ring portal-ring-1"></div>
            <div className="portal-ring portal-ring-2"></div>
            <div className="portal-ring portal-ring-3"></div>
            <div className="portal-center">
              <div className="magical-sparkles">
                <Sparkles className="sparkle sparkle-1" />
                <Stars className="sparkle sparkle-2" />
                <Heart className="sparkle sparkle-3" />
                <Wand2 className="sparkle sparkle-4" />
              </div>
              <h2 className="loading-text">
                동화 세계로의 여행을 준비하고 있어요...
              </h2>
            </div>
          </div>
          <div className="loading-particles">
            {[...Array(20)].map((_, i) => (
              <div key={i} className={`particle particle-${i + 1}`}></div>
            ))}
          </div>
        </div>
      )}

      {/* 메인 앱 화면 */}
      <div
        className={`fairy-tale-app ${isInitialLoading ? "hidden" : "visible"}`}
      >
        {/* 배경 이미지 */}
        <div className="background-overlay"></div>

        {/* 애니메이션 배경 요소들 */}
        <div className="floating-elements">
          <FloatingIcon icon={Sparkles} delay={0} />
          <FloatingIcon icon={Stars} delay={1} />
          <FloatingIcon icon={Heart} delay={2} />
          <FloatingIcon icon={Smile} delay={3} />
          <FloatingIcon icon={Book} delay={4} />
        </div>

        <Container fluid className="main-content">
          <Row className="justify-content-center">
            <Col xs={12} lg={10} xl={8}>
              {/* 헤더 섹션 */}
              <div className="text-center mb-5 header-section">
                <Card className="header-card border-0 shadow-lg">
                  <Card.Body className="p-4">
                    <div className="d-flex align-items-center justify-content-center gap-3 mb-3">
                      <Wand2 className="header-icon text-warning" />
                      <h1 className="gradient-text mb-0">Dream Tales</h1>
                      <Sparkles className="header-icon text-info spinning-icon" />
                    </div>
                    <p className="lead text-white mb-0">
                      ✨ 상상 속 이야기를 마법같은 그림으로 만들어보세요 ✨
                      <br />
                      <span className="text-pink">
                        한 문장이 아름다운 동화 일러스트가 됩니다
                      </span>
                    </p>
                  </Card.Body>
                </Card>
              </div>

              {/* 입력 섹션 */}
              <Row className="justify-content-center mb-5">
                <Col xs={12} lg={10}>
                  <Card className="input-card border-0 shadow-lg">
                    <Card.Body className="p-4">
                      {/* 동화 유형 선택 */}
                      <Form.Group className="mb-4">
                        <Form.Label className="text-white fs-5 fw-medium d-flex align-items-center gap-2 mb-3">
                          <Book className="text-warning" />
                          동화 유형 선택
                        </Form.Label>
                        <div className="story-type-radio-group">
                          <div className="radio-option">
                            <Form.Check
                              type="radio"
                              id="single-story"
                              name="storyType"
                              value="single"
                              checked={storyType === "single"}
                              onChange={(e) => setStoryType(e.target.value)}
                              className="custom-radio"
                            />
                            <label
                              htmlFor="single-story"
                              className="radio-label"
                            >
                              <div className="radio-content">
                                <Sparkles className="radio-icon" />
                                <div className="radio-text">
                                  <span className="radio-title">
                                    한 컷 동화 만들기
                                  </span>
                                  <span className="radio-description">
                                    한 장면으로 완성되는 동화
                                  </span>
                                </div>
                              </div>
                            </label>
                          </div>

                          <div className="radio-option">
                            <Form.Check
                              type="radio"
                              id="four-panel-story"
                              name="storyType"
                              value="four-panel"
                              checked={storyType === "four-panel"}
                              onChange={(e) => setStoryType(e.target.value)}
                              className="custom-radio"
                            />
                            <label
                              htmlFor="four-panel-story"
                              className="radio-label"
                            >
                              <div className="radio-content">
                                <Book className="radio-icon" />
                                <div className="radio-text">
                                  <span className="radio-title">
                                    4 컷 동화 만들기
                                  </span>
                                  <span className="radio-description">
                                    시작과 끝으로 만드는 네 개의 장면 동화
                                    스토리
                                  </span>
                                </div>
                              </div>
                            </label>
                          </div>
                        </div>
                      </Form.Group>

                      {/* 단일 이미지 입력 */}
                      {storyType === "single" && (
                        <Form.Group className="mb-4">
                          <Form.Label className="text-white fs-5 fw-medium d-flex align-items-center gap-2">
                            <Sparkles className="text-info" />
                            동화 이야기를 들려주세요
                          </Form.Label>
                          <Form.Control
                            as="textarea"
                            rows={4}
                            value={inputText}
                            onChange={(e) => setInputText(e.target.value)}
                            placeholder="예: 작은 토끼가 무지개 다리를 건너며 별들과 춤을 추었어요..."
                            className="story-input"
                            maxLength={200}
                          />
                          <div className="text-end text-muted mt-2">
                            {inputText.length}/200
                          </div>
                        </Form.Group>
                      )}

                      {/* 4컷 동화 입력 */}
                      {storyType === "four-panel" && (
                        <>
                          <Form.Group className="mb-4">
                            <Form.Label className="text-white fs-5 fw-medium d-flex align-items-center gap-2">
                              <Stars className="text-success" />
                              시작 장면을 들려주세요
                            </Form.Label>
                            <Form.Control
                              as="textarea"
                              rows={3}
                              value={startSceneText}
                              onChange={(e) =>
                                setStartSceneText(e.target.value)
                              }
                              placeholder="예: 작은 공주가 마법의 숲에서 길을 잃었어요..."
                              className="story-input"
                              maxLength={150}
                            />
                            <div className="text-end text-muted mt-2">
                              {startSceneText.length}/150
                            </div>
                          </Form.Group>

                          <Form.Group className="mb-4">
                            <Form.Label className="text-white fs-5 fw-medium d-flex align-items-center gap-2">
                              <Heart className="text-danger" />
                              마지막 장면을 들려주세요
                            </Form.Label>
                            <Form.Control
                              as="textarea"
                              rows={3}
                              value={endSceneText}
                              onChange={(e) => setEndSceneText(e.target.value)}
                              placeholder="예: 공주는 요정들의 도움으로 집으로 무사히 돌아갔어요..."
                              className="story-input"
                              maxLength={150}
                            />
                            <div className="text-end text-muted mt-2">
                              {endSceneText.length}/150
                            </div>
                          </Form.Group>
                        </>
                      )}

                      <Button
                        variant="primary"
                        size="lg"
                        onClick={
                          storyType === "single"
                            ? handleCreateSingleImage
                            : handleCreateFourPanelStory
                        }
                        disabled={
                          (storyType === "single" && !inputText.trim()) ||
                          (storyType === "four-panel" &&
                            (!startSceneText.trim() || !endSceneText.trim())) ||
                          isGenerating
                        }
                        className="w-100 generate-btn"
                      >
                        {isGenerating ? (
                          <>
                            <Spinner
                              animation="border"
                              size="sm"
                              className="me-2"
                            />
                            마법을 부리는 중...
                          </>
                        ) : (
                          <>
                            <Wand2 className="me-2" />✨
                            {storyType === "single"
                              ? "그림 만들기"
                              : "4컷 동화 만들기"}
                            ✨
                          </>
                        )}
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
              </Row>

              {/* 결과 섹션 */}
              {(isGenerating ||
                generatedImage ||
                generatedImages.length > 0) && (
                <Row ref={resultRef} className="justify-content-center">
                  <Col xs={12} lg={10}>
                    <Card className="result-card border-0 shadow-lg">
                      <Card.Body className="p-4">
                        <h2 className="text-center text-white mb-4 d-flex align-items-center justify-content-center gap-3">
                          <Book className="text-warning" />
                          당신의 동화가 완성되었어요!
                          <Sparkles className="text-info" />
                        </h2>

                        {isGenerating ? (
                          <div className="text-center py-5">
                            <div className="loading-container">
                              <div className="loading-spinner">
                                <Spinner animation="border" variant="info" />
                                <div className="loading-icon">
                                  <Wand2 className="text-warning" />
                                </div>
                              </div>
                              <p className="text-white fs-4 mt-4">
                                AI가 당신의 이야기를 그림으로 그리고 있어요...
                              </p>

                              {/* 시간 정보 박스 */}
                              <div className="time-info-container mt-4">
                                <div className="time-info-box">
                                  {waitingTime && (
                                    <div className="time-item expected-time">
                                      <span className="time-label">
                                        예상 시간
                                      </span>
                                      <span className="time-value">
                                        {waitingTime}초
                                      </span>
                                    </div>
                                  )}
                                  <div className="time-item running-time">
                                    <span className="time-label">
                                      진행 시간
                                    </span>
                                    <span className="time-value">
                                      {runningTime}초
                                    </span>
                                  </div>
                                </div>

                                {/* 진행률 바 (선택적) */}
                                {waitingTime && (
                                  <div className="progress-bar-container mt-3">
                                    <div className="progress-bar">
                                      <div
                                        className="progress-fill"
                                        style={{
                                          width: `${Math.min((runningTime / waitingTime) * 100, 100)}%`,
                                        }}
                                      ></div>
                                    </div>
                                    {/*
                                    <small className="progress-text">
                                      {Math.min(
                                        Math.round(
                                          (runningTime / waitingTime) * 100,
                                        ),
                                        100,
                                      )}
                                      % 완료
                                    </small>
                                    */}
                                  </div>
                                )}
                              </div>
                            </div>
                          </div>
                        ) : (
                          <div>
                            {/* 단일 이미지 결과 */}
                            {generatedImage && (
                              <Row className="justify-content-center mb-4">
                                <Col xs={12} md={8} lg={6}>
                                  <div className="text-center">
                                    <img
                                      src={generatedImage}
                                      alt="Generated illustration"
                                      className="img-fluid generated-image"
                                    />
                                  </div>
                                </Col>
                              </Row>
                            )}

                            {/* 4컷 동화 책 결과 */}
                            {generatedImages.length > 0 && (
                              <div className="storybook-container mb-4">
                                <div className="book-wrapper">
                                  <div
                                    className={`book ${currentPage > 0 ? "flipped" : ""}`}
                                  >
                                    {/* 책 커버 */}
                                    <div className="page cover-page">
                                      <div className="page-content">
                                        <div className="book-title">
                                          <Book className="title-icon" />
                                          <h3>나만의 동화책</h3>
                                          <div className="title-decoration">
                                            <Stars className="decoration-icon" />
                                            <Sparkles className="decoration-icon" />
                                            <Heart className="decoration-icon" />
                                          </div>
                                        </div>
                                      </div>
                                    </div>

                                    {/* 동화 페이지들 */}
                                    {generatedImages.map((imageUrl, index) => (
                                      <div
                                        key={index}
                                        className={`page story-page ${currentPage === index + 1 ? "active" : ""}`}
                                      >
                                        <div className="page-content">
                                          <div className="page-number">
                                            {index + 1}
                                          </div>
                                          <div className="story-image-container">
                                            <img
                                              src={imageUrl}
                                              alt={`Story panel ${index + 1}`}
                                              className="story-image"
                                            />
                                          </div>
                                          <div className="story-caption">
                                            {generatedTexts[index]}
                                          </div>
                                        </div>
                                      </div>
                                    ))}
                                  </div>

                                  {/* 책 넘기기 컨트롤 */}
                                  <div className="book-controls">
                                    <Button
                                      variant="outline-light"
                                      onClick={prevPage}
                                      disabled={currentPage === 0}
                                      className="page-nav-btn"
                                    >
                                      <ChevronLeft size={20} />
                                      이전
                                    </Button>

                                    <span className="page-indicator">
                                      {currentPage === 0
                                        ? "표지"
                                        : `${currentPage} / 4`}
                                    </span>

                                    <Button
                                      variant="outline-light"
                                      onClick={nextPage}
                                      disabled={
                                        currentPage === generatedImages.length
                                      }
                                      className="page-nav-btn"
                                    >
                                      다음
                                      <ChevronRight size={20} />
                                    </Button>
                                  </div>
                                </div>
                              </div>
                            )}

                            {/* 타이핑 효과로 나타나는 텍스트 */}
                            <Card className="story-text-card mb-4">
                              <Card.Body>
                                <p className="text-white fs-4 text-center mb-0 fw-medium">
                                  {displayText}
                                  {isTyping && (
                                    <span className="typing-cursor">|</span>
                                  )}
                                </p>
                              </Card.Body>
                            </Card>

                            {/* 다시 만들기 버튼 */}
                            <div className="text-center">
                              <Button
                                variant="warning"
                                size="lg"
                                onClick={resetAll}
                                className="restart-btn"
                              >
                                ✨ 새로운 이야기 만들기 ✨
                              </Button>
                            </div>
                          </div>
                        )}
                      </Card.Body>
                    </Card>
                  </Col>
                </Row>
              )}
            </Col>
          </Row>
        </Container>
      </div>
    </>
  );
}
