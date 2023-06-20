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


# STEP2/LESSON2 : building/deploying all app/db container using kubernatis

rebuild both services

    ./mvnw clean package -Dspring.profiles.active=local -Dmaven.test.skip=true

rebuild and push both images

    docker build -t ooo4u/user-service:1.0.0 ./
    docker push ooo4u/user-service:1.0.0
    docker build -t ooo4u/post-service:1.0.0 ./
    docker push ooo4u/post-service:1.0.0


Changes in postman collection: new ports 

    localhost:30081/posts
    localhost:30080/users

All yaml files are in project\manifest\

    pls verify PersistentVolum's hostPaths

Deploy/undeploy 
    
    project\manifest\apply.cmd
    project\manifest\delete.cmd


# STEP3/LESSON3 : 

rebuild both services

    ./mvnw clean package -Dspring.profiles.active=local -Dmaven.test.skip=true

rebuild and push both images

    cd ./user-service/ 
    docker build -t ooo4u/user-service:1.0.0 ./
    docker push ooo4u/user-service:1.0.0
    cd ..
    cd ./post-service/
    docker build -t ooo4u/post-service:1.0.0 ./
    docker push ooo4u/post-service:1.0.0

All yaml files are in project\manifest\

    pls verify PersistentVolum's hostPaths

Deploy/undeploy

    project\manifest\apply.cmd
    project\manifest\delete.cmd


## Task 4 details

to see the history of deployments:

      kubectl rollout history deployment.apps/posts-service -n=k8s-program

result will be like below:

        deployment.apps/posts-service
        REVISION  CHANGE-CAUSE
        1         <none>
        2         <none>

to roll back to previous version of deployment:

      kubectl rollout undo deployment.apps/posts-service -n=k8s-program


# STEP4/LESSON4 :

## Task 1 details

adding Chart.yaml, values.yaml and copy old *.yaml (lesson3 manifest folder) to templates

install

    cd project\helm.chart
    helm install k8s-program .

uninstall

    helm uninstall k8s-program

change service.replicaCount in  values.yaml

    helm install k8s-program .


## Task 2 details

adding _helpers.tpl

manifest_config.yml uses values as labels from helm _helpers.tpl file.


# STEP5/LESSON5 :

## Install ingress controller using helm chart 
    
    helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
    helm repo update
    helm install k8s-ingress ingress-nginx/ingress-nginx --namespace ingress --set controller.replicaCount=2
    
not used / just to remind

    kubectl get ingress k8s-ingress -n=k8s-program
    kubectl describe ingress k8s-ingress -n=k8s-program
    kubectl get ingressClass
    kubectl get ValidatingWebhookConfiguration


## Change services users-service and posts-service to be type ClusterIP

    helm.chart/templates/manifest_posts-service.yml
    helm.chart/templates/manifest_users-service.yml

## Create ingress resource and route your traffic using rules.

    kubectl apply -f manifest_ingress.yml
    
## Configure rewrite-target of path using annotations

    annotations:
      nginx.ingress.kubernetes.io/rewrite-target: /$2
    
    paths:
          - path: /posts(/|$)(.*)


you will be able access 

    localhost/users/greeting
    localhost/users/users

    localhost/posts/greeting
    localhost/posts/posts

