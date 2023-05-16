@rem mvnw clean package -Dspring.profiles.active=local -Dmaven.test.skip=true -f pom.xml
docker build -t ooo4u/post-service:1.0.0 ./
docker push ooo4u/post-service:1.0.0