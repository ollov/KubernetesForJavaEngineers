kubectl apply -f manifest_ns.yml
kubectl apply -f manifest_posts-db.yml
kubectl apply -f manifest_users-db.yml
kubectl apply -f manifest_posts-service.yml
kubectl apply -f manifest_users-service.yml

kubectl get all -n k8s-program
