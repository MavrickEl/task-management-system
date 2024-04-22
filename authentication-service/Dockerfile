FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /authentication-service

COPY . .

RUN mvn clean package

FROM openjdk:17

WORKDIR /authentication-service

COPY --from=build /authentication-service/target/authentication-service-0.1.jar /authentication-service/app.jar
EXPOSE 8085
CMD ["java", "-jar", "app.jar"]
