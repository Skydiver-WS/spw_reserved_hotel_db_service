FROM openjdk:21
LABEL authors="Aleksandr"

WORKDIR /app

COPY target/db_app_service-*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
