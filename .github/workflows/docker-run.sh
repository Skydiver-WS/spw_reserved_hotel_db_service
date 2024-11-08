docker push skydiverkhv/db_service:latest
docker run -d -p 8080:8080 skydiverkhv/db_service:latest --name db_service