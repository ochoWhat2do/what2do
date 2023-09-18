# what2do(오늘 뭐먹지?) 프로젝트 

## 목차
#### 1. [프로젝트 개요](# 1.프로젝트 개요)
* [프로젝트명](#프로젝트명)
* [프로젝트 요구 사항](#프로젝트-요구-사항)
#### 2. [기술 스택](#2.기술 스택)
#### 3. [프로젝트 명세](#프로젝트-명세)
* [ERD](#erd)
* [API 명세](#api-명세)
* [프로젝트 프레젠테이션 정보](#프로젝트 프레젠테이션 정보)
#### 4. [팀원소개](#팀원 소개)
#### 5. [개발 과정](#개발 과정)
#### 6. [트러블 슈팅](#트러블 슈팅)
#### 7. [팀원소개](#팀원 소개)

---

## 1. 프로젝트 개요
### 프로젝트 소개
> 2tWasSummer Project

### 프로젝트 요구 사항
<details>
<summary>필수 기능</summary>

#### 필수 기능 1
* 다음 지역 API 호출 

#### 필수 기능 2
* 리뷰 및 댓글 기능

</details>

<details>
<summary>추가 기능</summary>

#### 추가 기능 1
* 기능1 설명

#### 추가 기능 2
* 기능 2 설명

</details>

---

## 2. 기술 스택

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"><img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=&logoColor=white"><img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"><img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/Query DSL-0769AD?style=for-the-badge&logo=&logoColor=white"><img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white">
<br>
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"><img src="https://img.shields.io/badge/CSS-1572B6?style=for-the-badge&logo=css3&logoColor=white"><img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
<br>
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"><img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white"><img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">

---

## 3. 프로젝트 명세

### ERD

![team8_ocho_db](https://github.com/ochoWhat2do/what2do/assets/42510512/921c8a4b-1372-46cf-9456-1652dea10b7a)

### API 명세

https://api.what2do.shop/swagger-ui/index.html

### 홈페이지

https://www.what2do.co.kr/

---

## 4. 서비스 아키텍처

![시스템아키텍처구성도_작업파일_프론트230913](https://github.com/ochoWhat2do/what2do/assets/42510512/bcef6364-25c8-4186-abb1-bf68604491e1)

## 5. 개발 과정 및 기술적 의사결정

#### 5-1. 개발과정 

- SPRING 프레임워크 환경설정
- 사용자 관련 테이블 생성 및 로그인 기능 개발
- 백엔드 가게 조회 (수동 조회), 리뷰 작성 기능 개발
- 백엔드 API 조회 기능 개발 (카카오 로컬 API 활용)
- 프론트엔드 환경설정 및 언어 선택(react, next.js)



#### 5-2. 기술적 의사결정(가게 데이터를 어떻게 받아올 것인가?) 

가게 데이터를 받아와야한다 -> 스케쥴러를 실행하여 한번에 받는다
-> 최신화는 어떻게 할까?

1. 자체 DB 활용하면서, BATCH를 활용하여 INSERT, UPDATE, DELETE 동작을 주기적으로 실행
2. 실시간으로 API 데이터를 조회하되,  자체DB에 데이터를 추가-> 예외 상황에 대한 처리 추가

2안을 채택하여 실시간으로 데이터를 조회할 때마다, 테이블에 가게 데이터 수집 
-> API 데이터를 조회하는 테이블과 관계가 있는 가게 테이블로 분리 
-> API 연결이 안될때를 대비한 예외처리 가능

#### 5-3. 기술적 의사결정(이미지를 다중 첨부하는 방법)

이미지를 여러건 업로드 해야한다 -> 기능 구현 -> 프론트에서 파일명이 필요한 경우가 있다 -> 이미지 url에서 문자열을 Split 하여 파일명 추출 -> url이 정형화 되어있지 않다면?

1. 첨부파일 테이블을 따로 분리하여 가게 정보 조회시 join 구문을 사용한다.
2. MYSQL에서 지원하는 JSON 타입을 선언 후 JSON 문자열에 URL과 파일정보를 구분하여 저장

2안을 채택하여 실시간으로 데이터를 조회할 때마다, 자체DB에 가게 데이터 수집 API 데이터를 조회하는 테이블과 관계가 있는 가게 테이블로 분리 -> API 연결이 안될때를 대비한 예외처리 가능


## 6. 트러블 슈팅

#### 자주 사용되는 api 데이터 조회 시 캐싱 처리

캐싱을 사용한 API 성능 개선

(1) kakao api 호출 시 검색한 값들을 하나하나 select 문을 날려 DB에 있는지 확인 후 없으면 DB에 insert 하도록 구현
- 검색할 때 마다 select문 호출로 조회 속도가 느려지는 현상 발생, 검색 값을 캐시 메모리에 저장하여 무분별한 select 문 최소화

![0918_캐싱1](https://github.com/ochoWhat2do/what2do/assets/42510512/ed700217-1ba2-445f-a7e1-2955d8edb481)


(2) 리소스 과부하를 막기 위해 스케쥴러를 통한 주기적인 캐시 메모리 정리

![0918_캐싱2](https://github.com/ochoWhat2do/what2do/assets/42510512/f99ba16f-cd0d-46d4-b7cb-83cbd4dae4f3)


(3) 캐시메모리 정리 후에 조회 시 select 문이 다시 동작함을 알 수 있음

![0918_캐싱3](https://github.com/ochoWhat2do/what2do/assets/42510512/8dfb6f1d-3df8-4a8b-b6b1-83388f6518b8)


(4) 캐시처리를 이용하여 어느정도의 성능이 개선되었나 ?
  - 동일한 검색어를 가지고 조회하여 속도 확인
  - 972ms -> 105ms : 약 90% 의 성능 개선, 190ms -> 91ms : 약 50% 의 성능 개선
  - 즉 최소 50% 에서 90% 까지의 성능 개선 효과

- 첫번째 사례 
 - 처리 전 
![0918_캐싱_1_변화전](https://github.com/ochoWhat2do/what2do/assets/42510512/f6078fe9-b59b-4cf9-8472-d1509e603222)
 
 - 처리 후 

![0918_캐싱_1_변화후](https://github.com/ochoWhat2do/what2do/assets/42510512/d60f26b1-758e-4538-8a02-887f6a91cb6d)

- 두번째 사례 
- 처리 전
  ![0918_캐싱_2_변화전](https://github.com/ochoWhat2do/what2do/assets/42510512/b167959b-afb4-4659-a471-7239b4b30f5a)
- 처리 후
  ![0918_캐싱_2_변화후](https://github.com/ochoWhat2do/what2do/assets/42510512/ccdade54-c606-48f7-9c9d-4063995a9cec)

## 7. 팀원 소개

<table>
  <tbody>
    <tr>
      <td style="align:center"><a href=""><img src="" style="width:100px;" alt=""/><br /><sub><b>팀장 : 김태훈 </b></sub></a><br /></td>
      <td style="align:center"><a href=""><img src="" style="width:100px;" alt=""/><br /><sub><b>팀원 : 최신혜 </b></sub></a><br /></td>
      <td style="align:center"><a href=""><img src="" style="width:100px;" alt=""/><br /><sub><b>팀원 : 이승현 </b></sub></a><br /></td>
      <td style="align:center"><a href=""><img src="" style="width:100px;" alt=""/><br /><sub><b>팀원 : 안정민 </b></sub></a><br /></td>
     <tr/>
  </tbody>
</table>

👨‍💻 김태훈 -  [Github](https://github.com/sxi8022)<br>
👨‍💻 최신혜 -  [Github](https://github.com/choisinhye96)<br>
👨‍💻 이승현 -  [Github](https://github.com/SH-Lee2023)<br>
👩‍💻 안정민 -  [Github](https://github.com/MI-Ryeon)<br>
