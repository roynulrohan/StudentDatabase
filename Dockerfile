FROM maven:3.8-amazoncorretto-17 as build

COPY /StudentDatabaseAPI/src /app/src
COPY /StudentDatabaseAPI/pom.xml /app

RUN mvn -f /app/pom.xml clean package

FROM openjdk:17-alpine

COPY --from=build /app/target/StudentDatabaseAPI-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "java","-jar","app.jar" ]