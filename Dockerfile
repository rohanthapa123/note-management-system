    FROM openjdk:22

    EXPOSE 8080

    ADD target/Notes-Management-System.jar app.jar

    ENTRYPOINT ["java" , "-jar" , "/app.jar"]

