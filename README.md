# what2do(오늘 뭐하지?) 프로젝트 

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

![erd0918](https://github.com/ochoWhat2do/what2do/assets/42510512/96b4c32c-7371-493d-abac-cdd3d6cd7ba3)

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
현재 api 의 한계로 한 페이지에 15개의 게시글이 조회가 가능하지만 추후 데이터가 쌓였을 땐, 
쌓여진 DB 를 바탕으로 검색을 진행한다면 연결 속도는 점점 떨어질 것으로 예상
현재는 게시글이 적기 때문에 로딩 시간이 짧게는 200ms 부터 길게는 600ms 까지 보임

![561sec](https://github.com/ochoWhat2do/what2do/assets/42510512/611fa925-96fa-416b-b58f-732ceeb9ed9e)

최초 조회 시에는 query 문을 던지지만, 다시 검색 할 경우에는 DB 가 아닌 캐시 메모리에서 조회하는 것을 알 수 있음

![캐싱적용](https://github.com/ochoWhat2do/what2do/assets/42510512/e00ab5f9-a412-407e-8b5e-b86f4b4fbc3a)
이후 동일한 조회 결과에서는 속도가 개선된 모습을 볼 수 있음

![96sec](https://github.com/ochoWhat2do/what2do/assets/42510512/c85d09d1-86f6-49fe-bb0d-2437dcfcf895)

(1) 서버 과부하를 막기 위해 스케줄링을 도입해 주기적으로 캐시 메모리 정리
![캐싱메모리제거](https://github.com/ochoWhat2do/what2do/assets/42510512/d784d8fe-b304-4517-b6ea-f4f208fac644)

(2) 캐시 메모리 삭제 후 동일하게 검색 시 다시 select 하는 것을 볼 수 있음
![캐싱삭제후](https://github.com/ochoWhat2do/what2do/assets/42510512/c3c6e9a2-6b58-49ac-bf85-0484fcfa8326)

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
