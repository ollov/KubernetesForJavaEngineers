# KubernetesForJavaEngineers
Trainings: Kubernetes for Java Engineers

# postgresql
docker build -t postgresql-image-users . -f DockerfileDBUsers
docker run -p 5433:5432 --name postgresql-container-users -d postgresql-image-users

docker build -t postgresql-image-posts . -f DockerfileDBPosts
docker run -p 5434:5432 --name postgresql-container-posts -d postgresql-image-posts

# post service

port : 8081
./mvnw spring-boot:run

# user service

port : 8082
./mvnw spring-boot:run