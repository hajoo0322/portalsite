# portalsite

## 📝소개

--------------
### 프로젝트 소개
* portalsite 미니 프로젝트
* 프로젝트 기간: 2025.04.24 ~ 2025.05.09
* BE 5명
* 대용량 데이터를 빠르게 검색할 수 있는 포털사이트 검색 플랫폼입니다.

### 주요 기능
* 통합 검색 기능
    * 뉴스, 블로그, 카페 게시글을 대상으로 한 통합 검색 API 제공
    * MySQL Full-Text Index → Elasticsearch로 전환하여 검색 정확도 및 속도 개선
    * 인기 검색어 및 자동완성 기능 구현 (사용자 클릭/체류 시간 기반 집계)
<br/>
  
* 콘텐츠 관리 기능
  * 블로그/카페 생성 및 게시판, 게시글, 댓글 등록/조회/수정/좋아요 지원
  * 뉴스 콘텐츠는 리포터 권한을 통해 카테고리/기사 등록 가능
<br/>

* 카페 등급 및 멤버십 관리
  * 카페 회원 등급 생성 및 자동 승급 조건 설정
  * 중복 검사 및 닉네임 변경 등 사용자 정보 관리 기능 제공
<br/>

* 사용자 인증 및 권한 분리
  * 운영자/일반회원/리포터 등 역할에 따라 JWT 기반 인증 처리
  * 역할별 접근 제어를 통해 리소스 보호
<br/>

## ⚙️ 기술 스택

-------------
### Back-end
Java, Spring Boot, JPA, QueryDSL, MySQL, Docker, Redis, Kafka, Elasticsearch,
Spring Scheduler, Spring Event, Spring Security, JJWT

### Tools
Github, Slack, Notion
<br/>
<br/>

## 🗺️ ERD

-------------
작성 ERD는 아래 링크에서 확인 가능합니다.
<br/>👉🏻 [ERD 바로 가기](https://www.erdcloud.com/d/gPhB7S246SCxLTKf3)
<br/>
<br/>

## 🗂️ APIs

-------------
작성한 API는 아래 링크에서 확인 가능합니다.
<br/>👉🏻 [API 바로 가기](https://documenter.getpostman.com/view/40135309/2sB2qZFNba)
<br/>
<br/>

## 🔑️기술적 이슈 및 해결 과정
* [검색 응답 시간 목표 설정](https://sour-furniture-040.notion.site/1e5589af8800801faf87d67eb476f590?pvs=4)
* [N+1문제 해결 및 성능개선(1차)](https://sour-furniture-040.notion.site/N-1-1-1e5589af880080b4b8a2cfe36a5c15eb?pvs=4)
* [ElasticSearch 선택 이유](https://sour-furniture-040.notion.site/1ec589af880080bdae8cc38546d14f6d?pvs=4)