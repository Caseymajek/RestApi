FROM openjdk:17
WORKDIR /app
COPY target/RestfulApi-0.0.1-SNAPSHOT.jar /app/RestfulApi-app.jar
EXPOSE 8081
CMD ["java", "-jar", "RestfulApi-app.jar"]