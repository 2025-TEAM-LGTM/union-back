# UniON-back
AI 기반 포트폴리오 매칭을 활용한 대학생 팀빌딩 플랫폼 **UniON**의 백엔드 서버입니다.

Spring Boot 기반 REST API 서버로  
사용자 인증, 팀 모집 공고, 포트폴리오 관리, AI 매칭 기능을 제공합니다.

주요 기술 스택:
- Java 17+
- Spring Boot 4.0
- Spring Data JPA
- Lombok
- JWT
- PostgreSQL RDS
- Maven

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
│   │   │   ├── service       # 비즈니스 로직 처리 (도메인 로직, 트랜잭션 관리)
|   |   |   ├── event         # 공고, 포트폴리오 이벤트 발생 시 fast api를 통해 AI 코드와 통신
│   │   │   ├── repository    # JPA Repository (DB 접근)
│   │   │   ├── dto           # 요청, 응답 dto
│   │   │   ├── exception     # 예외 처리
│   │   │   ├── util          # 여러 계층에서 공통으로 사용하는 유틸리티 클래스
│   │   │   ├── enums         # enum 모음
|   |   |   └── global        # 공통 에러 핸들러, 공통 응답 코드
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

## 🛠️ 설정
### 1) application.yml
로컬 환경에서 사용할 `application.yml` 파일을 생성한 뒤, 아래 값을 입력합니다.

```yaml
spring:
  datasource:
    url: database url
    username: your_name
    password: your_password
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
    show-sql: true

  sql:
    init:
      mode: never

# jwt
jwt:
  secret: your_jwt_secret

# s3
cloud:
  aws:
    region: ap-northeast-2
    s3:
      bucket: your_bucket_name
      public-base-url: your_s3_base_url
      credentials:
        access-key: your_s3_access_key
        secret-key: your_s3_secret_key
```

### 2) application-prod.yml
레포지토리에 있는 application-prod.yml을 사용하되, AWS EC2 서버 환경에 필요한 환경변수를 직접 설정해야 합니다.

필요한 환경변수는 다음과 같습니다.
```
DB_URL=
DB_PASSWORD=
JWT_SECRET=
AWS_REGION=
S3_BUCKET=
S3_ACCESS_KEY=
S3_KEY=
S3_PUBLIC_BASE_URL=
```

## 🚀 주요 EndPoint



## 🔗 외부 서비스 연동
**UniON-AI (FastAPI)**
포트폴리오 임베딩 및 역할 매칭 알고리즘을 활용한 팀매칭 기능을 위해 연결합니다.
