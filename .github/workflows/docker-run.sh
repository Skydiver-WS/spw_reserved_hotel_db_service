docker rm -f db_service

docker push skydiverkhv/db_service:v1.1.1

docker run -d --name db_service --network skydiverkhv/db_service:v1.1.1
