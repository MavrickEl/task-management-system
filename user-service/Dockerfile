FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /user-service

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /user-service

COPY --from=build /user-service/target/user-service-0.1.jar /user-service/app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
