FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} what2do.jar

# 개발 프로파일로 설정
ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "/what2do.jar"]