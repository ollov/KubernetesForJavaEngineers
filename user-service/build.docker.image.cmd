@rem mvnw clean package

docker build -t ooo4u/user-service:1.0.0 ./
docker push ooo4u/user-service:1.0.0