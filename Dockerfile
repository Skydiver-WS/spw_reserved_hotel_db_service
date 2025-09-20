FROM openjdk:21
LABEL authors="Aleksandr"

WORKDIR /app

# Используем ARG для передачи версии при сборке
ARG VERSION=1.0.2
COPY target/db_app_service-${VERSION}.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
