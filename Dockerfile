FROM maven:3.9.7 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21
WORKDIR /app

COPY --from=build /app/target/Notes-Management-System.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java" , "-jar" , "app.jar"]

