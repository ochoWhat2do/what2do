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

![team8_ocho_erd (1)](https://github.com/ochoWhat2do/what2do/assets/42510512/014d5ba3-0e59-45c5-b550-370005754949)

### API 명세

API 명세

### 프로젝트 프레젠테이션 정보



---

## 4. 서비스 아키텍처
- 사용자 관점
![아키텍처3](https://github.com/ochoWhat2do/what2do/assets/42510512/0b2d45e2-17bd-4749-b7e8-39998a4073dc)

- BACK END 아키텍처
  ![아키텍처4](https://github.com/ochoWhat2do/what2do/assets/42510512/6cfea98d-41de-4195-96ae-3a62d0b5b8d9)

## 5. 개발 과정
- SPRING 프레임워크 환경설정
- 사용자 관련 테이블 생성 및 로그인 기능 개발
- 백엔드 가게 조회 (수동 조회), 리뷰 작성 기능 개발
- 백엔드 API 조회 기능 개발 
- 프론트엔드 환경설정 및 언어 선택

## 6. 트러블 슈팅
처음에 자체 DB를 활용하여, API 정보를 일괄적으로 DB에 저장할 생각을 했다.
-> 그러나, 데이터의 최신화 이슈가 있었다
-> 실시간 조회기능을 진행하되
-> 실시간 조회기능이 동작하지 않을 때를 대비하여 조회 메소드 실행 시 로컬 DB에 데이터를 조금 씩 저장
-> 데이터가 많이 쌓이면서 API에 접근이 되지않을때에도 원하는 가게의 정보를 조회 가능
-> API호출정보를 수신하여 실시간 조회에 사용하는 가게 테이블과 연관관계(찜이나, 사용자)가 적용된 가게 테이블로 2가지 분리하여 잘못 동작될 가능성을 방지
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
