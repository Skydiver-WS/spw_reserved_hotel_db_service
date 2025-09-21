#!/bin/bash

# Достаём версию из pom.xml
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

echo "Using version: $VERSION"

# Останавливаем и удаляем старый контейнер
docker rm -f db_service || true

# Запускаем новый контейнер
docker run -d -p 8081:8081 --name bokking-db-app skydiverkhv/db_service:"$VERSION"

echo "Container db_service started with version $VERSION"