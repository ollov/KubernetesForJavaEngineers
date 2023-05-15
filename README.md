# KubernetesForJavaEngineers
Trainings: Kubernetes for Java Engineers

# STEP0 : building/deploying each app/db container separately
___

# PRE
docker network create my-network

# postgresql

    docker build -t postgresql-image-users . -f DockerfileDBUsers
    docker run -p 5433:5432 --network my-network --name postgresql-container-users -d postgresql-image-users

    docker build -t postgresql-image-posts . -f DockerfileDBPosts
    docker run -p 5434:5432 --network my-network --name postgresql-container-posts -d postgresql-image-posts

# post service

to verify on local :

    ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

to build

    ./mvnw clean package -Dspring.profiles.active=local
    docker build -t ooo4u/post-service:1.0.0 ./
    docker login
    docker push ooo4u/post-service:1.0.0
    docker run -ti --network my-network -e SPRING_PROFILES_ACTIVE=docker -e DB_URL=jdbc:postgresql://postgresql-container-posts:5432/k8training -e DB_USER=user -e DB_PASSWORD=password -e USERS_URL=http://container-user:8080 -p 8081:8080 --name container-post -d ooo4u/post-service-1.0.0


# user service

to verify on local :

    ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

to build

    ./mvnw clean package -Dspring.profiles.active=local
    docker build -t ooo4u/user-service:1.0.0 ./
    docker login
    docker push ooo4u/user-service:1.0.0
    docker run -ti --network my-network -e SPRING_PROFILES_ACTIVE=docker -e DB_URL=jdbc:postgresql://postgresql-container-users:5432/k8training -e DB_USER=user -e DB_PASSWORD=password -p 8082:8080 --name container-user -d ooo4u/user-service-1.0.0


___

# STEP1/LESSON1 : building/deploying all app/db container using docker-compose

    docker-compose up
    docker-compose down

