FROM openjdk:21
LABEL authors="Aleksandr"

WORKDIR /app

COPY target/db_app_service-1.0.7.jar app.jar

# Открываем порт, который использует ваше приложение
EXPOSE 8080

# Команда для запуска JAR-файла
ENTRYPOINT ["java", "-jar", "app.jar"]