FROM openjdk:17-jdk-alpine
ARG JAR_FILE=builds/libs/*.jar
COPY ${JAR_FILE} /what2do.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/what2do.jar"]