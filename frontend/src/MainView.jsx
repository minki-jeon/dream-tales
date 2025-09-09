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
import { Sparkles, Wand2, Book, Stars, Heart, Smile } from "lucide-react";
import "./css/MainView.css";
import axios from "axios";

export function MainView() {
  const [inputText, setInputText] = useState("");
  const [isGenerating, setIsGenerating] = useState(false);
  const [generatedImage, setGeneratedImage] = useState(null);
  const [displayText, setDisplayText] = useState("");
  const [isTyping, setIsTyping] = useState(false);
  const [isInitialLoading, setIsInitialLoading] = useState(true);
  const resultRef = useRef(null);

  // 초기 로딩 애니메이션 제어
  useEffect(() => {
    const timer = setTimeout(() => {
      setIsInitialLoading(false);
    }, 3000); // 3초 후 메인 화면 표시

    return () => clearTimeout(timer);
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

  // 이미지 생성 (backend에서 api 연동)
  const handleCreateImageClickBtn = async () => {
    if (!inputText.trim()) return;
    setIsGenerating(true);

    try {
      const response = await axios.post("/api/create/dalle2", {
        text: inputText,
      });
      console.log(response.data);

      setTimeout(() => {
        // 샘플 이미지 URL (실제로는 API 응답)
        setGeneratedImage(response.data.image_path);
        setIsGenerating(false);

        // 결과 섹션으로 스크롤
        setTimeout(() => {
          resultRef.current?.scrollIntoView({
            behavior: "smooth",
            block: "start",
          });
          // 타이핑 효과 시작
          setTimeout(() => typeWriterEffect(inputText), 500);
        }, 100);
      }, 3000);
    } catch (err) {
      console.log("[Error] handleCreateImageClickBtn() : ", err);
    }
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
                      <Form.Group className="mb-4">
                        <Form.Label className="text-white fs-5 fw-medium d-flex align-items-center gap-2">
                          <Book className="text-warning" />
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

                      <Button
                        variant="primary"
                        size="lg"
                        onClick={handleCreateImageClickBtn}
                        disabled={!inputText.trim() || isGenerating}
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
                            <Wand2 className="me-2" />✨ 그림 만들기 ✨
                          </>
                        )}
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
              </Row>

              {/* 결과 섹션 */}
              {(isGenerating || generatedImage) && (
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
                            </div>
                          </div>
                        ) : (
                          <div>
                            {/* 생성된 이미지 */}
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

                            {/* 타이핑 효과로 나타나는 텍스트 */}
                            <Card className="story-text-card mb-4">
                              <Card.Body>
                                <p className="text-white fs-4 text-center mb-0 fw-medium">
                                  "{displayText}"
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
                                onClick={() => {
                                  setGeneratedImage(null);
                                  setDisplayText("");
                                  setInputText("");
                                  window.scrollTo({
                                    top: 0,
                                    behavior: "smooth",
                                  });
                                }}
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
