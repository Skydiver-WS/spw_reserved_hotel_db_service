#!/bin/bash
# Pull the latest image from Docker Hub
docker pull skydiverkhv/db_service:latest

# Stop and remove the current container
docker stop app_container || true
docker rm app_container || true

# Start a new container with the updated image
docker run -d --name app_container -p 3000:3000 skydiverkhv/db_service:latest
