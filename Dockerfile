FROM maven:3.9-amazoncorretto-23 as builder
LABEL authors="Aleksandr"

WORKDIR /app

COPY target/db_app_service-*.jar app.jar

EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=virtual-thread
ENTRYPOINT ["java", "-jar", "app.jar"]
