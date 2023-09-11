FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /what2do/
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/what2do.jar"]