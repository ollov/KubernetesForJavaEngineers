# KubernetesForJavaEngineers
Trainings: Kubernetes for Java Engineers

# postgresql
docker build -t my-postgresql-image .

docker run -p 5432:5432 --name my-postgresql-container -d my-postgresql-image

# post service

port : 8081
./mvnw spring-boot:run

# user service

port : 8082
./mvnw spring-boot:run