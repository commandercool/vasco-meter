FROM maven:3.8.1-openjdk-11
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:11.0.11-jdk-slim
WORKDIR /app
COPY --from=0 /app/target/*.jar app.jar
CMD java -jar app.jar
