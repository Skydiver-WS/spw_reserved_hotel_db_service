FROM openjdk:21
LABEL authors="Aleksandr"

WORKDIR /app

ARG APP_VERSION
COPY target/db_app_service-${APP_VERSION}.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
