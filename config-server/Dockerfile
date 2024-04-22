FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /config-server

COPY . .

RUN mvn clean package

FROM openjdk:17

WORKDIR /config-server

COPY --from=build /config-server/target/config-server-0.1.jar /config-server/app.jar
EXPOSE 8888
CMD ["java", "-jar", "app.jar"]
