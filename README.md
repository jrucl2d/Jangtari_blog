# Jangtari_blog

장성균(장따리)을 위한 블로그

## 1. 동기 및 개발 목표

- 평소 감성쟁이임을 자처하는 친구 장성균(a.k.a. 장따리)이 감성 넘치는 똥글을 사진과 함께 게시할 수 있는 블로그를 하나 운영하기를 원한다는 사실을 전달 받았다.
- 백엔드 공부를 하면서 가장 많이 만들어본 토이 프로젝트가 바로 블로그 프로젝트였기 때문에 DB 설계 및 전체적인 서비스 로직 설계에 있어서 익숙함이 있었다.
- (스프링부트 + Thymeleaf의 Spring MVC 기반 블로그 프로젝트 : <https://github.com/jrucl2d/Start_Spring_Boot>)
- (스프링부트 + jsp의 Spring MVC 기반 블로그 프로젝트 : <https://github.com/jrucl2d/SpringBoot_Blog>)
- (nodejs + nunjucks 템플릿 엔진 기반 블로그 프로젝트 : <https://github.com/jrucl2d/ggbook_production>)
- DB 설계부터 모든 기능 구현, 배포까지 혼자서 진행해본 경험이 없었기 때문에 도전하는 마음으로 프로젝트를 시작하게 되었다.
- 최종적인 목표는 계획한 기능을 모두 구현하고 클라우드에 배포해서 실사용 가능하도록 하는 것이다.

## 2. 기술 스택

### 2.1 프론트엔드

- React.js
- Redux
- Bootstrap

### 2.2 백엔드

- SpringBoot
- Spring Data JPA
- QueryDsl
- Spring Security : JWT 로그인 구현, 토큰은 쿠키에 저장
- Google Drive API : 이미지 저장소로 사용
- Mysql
- Redis : JWT 로그인 관련

## 3. DB 설계

![db](./image/erd.png)

## 4. 주요 기능 소개

## 5. TODO LIST

### 5.1 공통 기능

- [x] 회원가입
- [x] 로그인
- [x] 로그아웃
- [x] 회원 정보 변경(닉네임, 비밀번호 변경 가능)
- [x] 댓글 추가
- [x] 대댓글 추가(1계층 대댓글까지만 가능)
- [x] 댓글 수정(본인의 경우에만)
- [x] 댓글 삭제(본인의 경우에만)
- [x] 제목, 내용, 해시태그로 게시글 검색
- [ ] 회원 탈퇴

### 5.2 장성균 전용 기능

- [x] 개인 소개 변경(닉네임, 소개글, 사진)
- [x] 카테고리 추가
- [x] 카테고리 정보 변경(카테고리 제목, 사진 변경 가능)
- [x] 카테고리 삭제
- [x] 게시글 템플릿 선택(현재 두 개)
- [x] 게시글 작성(제목, 해시태그 최대 5개, 본문, 사진)
- [x] 게시글 수정(제목, 본문, 템플릿, 해시태그, 사진 변경 가능)
- [x] 게시글 삭제

### 5.3 내부 기능

- [x] 페이징 처리
- [x] JWT 로그인 구현
  - [x] Spring Security 설정파일에 jwt 사용하도록 configure 메소드 설정
  - [x] JWT 토큰 발급 관련 클래스 구현(JWTTokenProvider)
  - [x] JWT 인증 관련 filter 클래스 구현(JWTAuthenticationFilter)
- [x] Google Drive API 연동
  - [x] Google API 프로젝트 생성
  - [x] 사진 저장 관련 로직에 Google Drive API 추가
- [x] 배포할 클라우드 환경 세팅 : 오라클 클라우드
- [x] 도메인 설정 : 무료 도메인
- [x] nginx를 사용한 포트 포워딩 : 80(HTTP) -> 8080(톰켓 서버 포트)
- [ ] HTTPS 적용
