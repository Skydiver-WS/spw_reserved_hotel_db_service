docker rm -f db_service

docker push skydiverkhv/db_service:v1.1.0

docker run -d --name db_service --network kafka_app-network -p 8080:8080 -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 skydiverkhv/db_service:v1.0.8
