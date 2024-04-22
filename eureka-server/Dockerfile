FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /eureka-server

COPY . .

RUN mvn clean package

FROM openjdk:17

WORKDIR /eureka-server

COPY --from=build /eureka-server/target/eureka-server-0.1.jar /eureka-server/app.jar
EXPOSE 8761
CMD ["java", "-jar", "app.jar"]
