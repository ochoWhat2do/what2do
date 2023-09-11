FROM openjdk:17-jdk
COPY ./build/libs/what2do-0.0.1-SNAPSHOT.jar what2do.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "what2do.jar"]