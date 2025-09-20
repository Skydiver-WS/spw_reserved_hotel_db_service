#!/bin/bash

# Достаём версию из pom.xml
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

echo "Using version: $VERSION"

# Останавливаем и удаляем старый контейнер
docker rm -f db_service || true

# Запускаем новый контейнер
docker run -d \
  --name db_service \
  --network your_network_name \
  -p 8080:8080 \
  skydiverkhv/db_service:"$VERSION"

echo "Container db_service started with version $VERSION"