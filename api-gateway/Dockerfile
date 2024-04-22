FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /gateway-service

COPY . .

RUN mvn clean package

FROM openjdk:17

WORKDIR /gateway-service

COPY --from=build /gateway-service/target/api-gateway-0.1.jar /gateway-service/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
