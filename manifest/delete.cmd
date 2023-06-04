kubectl delete -f manifest_users-service.yml
kubectl delete -f manifest_posts-service.yml
kubectl delete -f manifest_posts-db.yml
kubectl delete -f manifest_users-db.yml
kubectl delete -f manifest_config.yml 
kubectl delete -f manifest_secret.yml 
kubectl delete -f manifest_ns.yml

kubectl get all -n=k8s-program
