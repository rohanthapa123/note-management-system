FROM openjdk:17-jre-slim

ARG JAR_FILE=target/Notes-Management-System-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java" , "-jar" , "/app.jar"]

