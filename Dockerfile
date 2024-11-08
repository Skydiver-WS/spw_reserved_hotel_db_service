FROM openjdk:21
LABEL authors="Aleksandr"

WORKDIR /app

COPY target/reservstion_system_db_app-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт, который использует ваше приложение
EXPOSE 8080

# Команда для запуска JAR-файла
ENTRYPOINT ["java", "-jar", "app.jar"]