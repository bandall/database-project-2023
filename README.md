<div align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&height=250&color=80ea6e&fontColor=363636&text=%EC%A0%84%EC%9E%90%EC%B6%9C%EA%B2%B0%20%EB%8F%84%EC%9A%B0%EB%AF%B8%20%EC%84%9C%EB%B2%84" alt="header"/>
</div>

<div align="center">
    아주대 전자출결 도우미 스프링 서버
    <br><br>
    <a href="https://github.com/bandall/database-project-2023" target="_blank">
        <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Backend Spring Server - View Project">
    </a>
    <br>
    <a href="https://github.com/bandall/Spring-JWT-Login" target="_blank">JWT 로그인 서버 보러가기</a>

</div>

## 🛠️ 기술 스택 🛠️

<div align="center">
    <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
    <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
    <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
    <br>
    <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
    <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
    <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
    <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
</div>

<br>

## 🧰 개발 도구 🧰

<div align="center">
    <img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white">
    <img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
    <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">    
    <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>

## 서버 구조

<div align="center">
    <img src="https://github.com/bandall/database-project-2023/assets/32717522/200533db-efe9-4509-9d5e-ce9e18cc9dbd" width="600">
</div>

- JWT 로그인 서버에서 발급한 JWT 토큰을 통해 전자출결 도우미 서비스 서버에 접근할 수 있습니다.

<div align="center">
    <img src="https://github.com/bandall/database-project-2023/assets/32717522/71801864-0d43-4339-917d-0fd3c8574015" width="600">
</div>

- 위 사진은 JWT 토큰 발급 및 인증 과정 예싱입니다.

### 1. 알림 등록 과정

<div align="center">
    <img src="https://github.com/bandall/database-project-2023/assets/32717522/639ee632-a478-4630-96c4-6ecd2de01ab5" width="700">
</div>

### 2. 공강 시간 조회 과정

<div align="center">
    <img src="https://github.com/bandall/database-project-2023/assets/32717522/ac38600f-238b-479f-9b38-b4b1acc449c9" width="700">
</div>

### 3. JPA 쿼리 최적화

<div align="center">
    <img src="https://github.com/bandall/database-project-2023/assets/32717522/56ec4d72-9432-419c-bce3-239c06b696f7" width="500">
</div>

- 프로젝트 전반에 걸쳐 쿼리 성능을 최적화하기 위해 여러 번 수정을 거쳤습니다.
- 그 결과 전반적으로 3 ~ 4배의 성능 개선을 할 수 있었습니다.
- 위 사진은 시간표 조회 쿼리의 최적화 전 후의 비교 사진입니다.