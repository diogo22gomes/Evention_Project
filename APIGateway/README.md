API Gateway NGIX

# (OLD) API Gateway antiga 

Instalar NGIX Ingress

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx


RUN

kubectl apply -f api-gateway.yaml


-------------- API GATEWAY HTTPS --------------

-> CRIAR CHAVES (caso necessario):

# Gerar a chave privada
openssl genrsa -out tls.key 2048

# Gerar o certificado autoassinado
openssl req -new -x509 -key tls.key -out tls.crt -days 365 -subj "/CN=evention.local"

-> APLICA O SECRET (NECESSARIO):

# Criar secret no minikube
kubectl create secret tls nginx-tls-secret --cert=tls.crt --key=tls.key

kubectl get secrets

# Deploy da API Gateway
kubectl apply -f nginx-configmap.yaml
kubectl apply -f nginx-deployment.yaml
kubectl apply -f nginx-service.yaml

# PARA O DEPLOYMENT COM LIMITE DE CARGA
kubectl apply -f nginx-main-config.yaml

# get svc
kubectl get svc nginx-api-gateway

------------

# ROTAS DA API GATEWAY COM HTTPS

https://127.0.0.1:5010/user/

https://127.0.0.1:5010/event/

https://127.0.0.1:5010/userinevent/

https://127.0.0.1:5010/payment/

https://127.0.0.1:5010/location/

------------

# ROTAS DA API GATEWAY COM HTTPS (SWAGGER)

https://127.0.0.1:5010/user/api-docs/

https://127.0.0.1:5010/event/api-docs/

https://127.0.0.1:5010/userinevent/api-docs/

https://127.0.0.1:5010/payment/api-docs/

https://127.0.0.1:5010/location/api-docs/