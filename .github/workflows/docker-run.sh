docker rm -f db_service

docker push skydiverkhv/db_service:latest

docker run -d --name db_service --network kafka_app-network -p 8080:8080 -e SPRING_KAFKA_BOOTSTRAP_SERVERS=127.0.1.2:9092 skydiverkhv/db_service:latest
