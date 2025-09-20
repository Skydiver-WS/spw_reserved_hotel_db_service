# Достаём версию из pom.xml
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

echo "Using version: $VERSION"

docker rm -f db_service

docker push skydiverkhv/db_service:v"$VERSION"

docker run -d --name db_service --network skydiverkhv/db_service:"$VERSION"
