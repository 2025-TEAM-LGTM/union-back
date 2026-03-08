# UniON-back
AI 기반 포트폴리오 매칭을 활용한 대학생 팀빌딩 플랫폼 **UniON**의 백엔드 서버입니다.

Spring Boot 기반 REST API 서버로  
사용자 인증, 팀 모집 공고, 포트폴리오 관리, AI 매칭 기능을 제공합니다.

주요 기술 스택:
- Java 17+
- Spring Boot 4.0
- Spring Data JPA
- Lombok
- JWT 인증
- PostgreSQL RDS

## ✈️Architecture
<img width="4588" height="2835" alt="AWS (2025) horizontal framework - Page 1 UniON-Architecture" src="https://github.com/user-attachments/assets/d4d321c2-6ea2-4b77-8943-0764d45a126e" />

## 📁 Source Code 설명
```text
union-back
├── src/
│   ├── main/
│   │   ├── java/com/union/demo/
│   │   │   ├── config        # 전역 설정 (CORS, Web, Swagger, Bean 설정 등)
|   |   |   ├── jwt           # jwt 관련 설정
│   │   │   ├── security      # Spring Security, JWT 인증/인가, 필터
│   │   │   ├── controller    # REST API의 Endpoint
│   │   │   ├── service       # 
│   │   │   ├── repository    # JPA Repository (DB 접근)
│   │   │   ├── dto           # 요청, 응답 dto
│   │   │   ├── exception     # 예외 처리
│   │   │   ├── util          #
│   │   │   ├── enums         # enum 모음
|   |   |   └── global        #
│   │   └── resources/
|   |       ├── application-prod.properties  # 배포용 환경설정
│   │       └── application.properties  # Spring 환경 설정
│   └── test/
│       └── java/com/union/demo
│           └── UnionApplicationTests.java
├── Dockerfile
├── .gitattributes
├── .gitignore
├── run.log
├── pom.xml
├── mvnw
└── mvnw.cmd
└── README.md

```

## 🛠️ Build

## 🚀 주요 EndPoint

## 📦 외부 라이브러리, 오픈 소스
pom.xml에 포함된 라이브러리는 다음과 같습니다.
- Spring Boot
- Spring Data JPA (Hibername)
- Spring Security
- PostgreSQL
- Swagger
- lombok
- JJWT


## 🔗 외부 서비스 연동
**UniON-AI (FastAPI)**
포트폴리오 임베딩 및 역할 매칭 알고리즘을 활용한 팀매칭 기능을 위해 연결합니다.
