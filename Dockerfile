FROM openjdk:11.0.11-jdk-slim
WORKDIR /app
COPY /target/*.jar app.jar
CMD java -jar app.jar
