# UniON-back
AI 기반 포트폴리오 매칭을 활용한 대학생 팀빌딩 플랫폼 **UniON**의 백엔드 서버입니다.

Spring Boot 기반 REST API 서버로  
사용자 인증, 팀 모집 공고, 포트폴리오 관리, AI 매칭 기능을 제공합니다.

주요 기술 스택
- Java 17+
- Spring Boot 4.0
- Spring Data JPA
- Lombok
- JWT 인증
- PostgreSQL RDS

## Architecture
그림

## Main Features
### Authentication
- JWT 기반 로그인 / 회원가입
- Refresh Token Rotation
- Access Token 재발급

### Team Recruitment
- 프로젝트 팀원 모집 공고 작성
- 모집 역할(Role) 및 도메인 필터링
- D-Day 기반 모집 상태 관리

### Portfolio
- STAR 기반 포트폴리오 작성 및 관리

### AI Matching
- FastAPI 기반 AI 서비스와 통신하여 매칭 기능 제공

## Database Schema
ERD 사진

## Directory Structure

- AWS EC2
- AWS RDS (PostgreSQL)
- AWS S3


