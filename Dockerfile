FROM openjdk:17-jdk
COPY ./build/libs/what2do-0.0.1-SNAPSHOT.jar what2do.jar

WORKDIR /app

COPY build/libs/*.jar app.jar
COPY src/main/resources/application-ds.properties /app/application-ds.properties
COPY src/main/resources/application-s3.properties /app/application-s3.properties
COPY src/main/resources/application-key.properties /app/application-key.properties
COPY src/main/resources/application-oauth.properties /app/application-oauth.properties
COPY src/main/resources/application-template.properties /app/application-template.properties

# 개발 프로파일로 설정
ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "what2do.jar"]