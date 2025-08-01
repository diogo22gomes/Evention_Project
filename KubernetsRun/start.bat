@echo off
echo Iniciando cluster Kubernetes local com Minikube...
minikube start
echo.
echo Limpando volumes antigos (caso existam) para evitar estado Released...
kubectl delete pvc userservice-images-pvc --ignore-not-found
kubectl delete pv userservice-images-pv --ignore-not-found
kubectl delete pvc eventservice-images-pvc --ignore-not-found
kubectl delete pv eventservice-images-pv --ignore-not-found

echo.
echo Aplicando configuracoes do Kubernetes...

:: Aplicar todos os storages primeiro
kubectl apply -f databases\postgres-storage.yaml
kubectl apply -f databases\postgres-storage-user.yaml
kubectl apply -f databases\postgres-storage-userEvent.yaml
kubectl apply -f databases\postgres-storage-loc.yaml
kubectl apply -f databases\postgres-storage-pay.yaml
kubectl apply -f databases\redis-storage.yaml

kubectl apply -f services\eventservice-images-storage.yaml
kubectl apply -f services\userservice-images-storage.yaml

echo.
echo Aplicando bancos de dados e Redis...
kubectl apply -f databases\postgres-deployment.yaml
kubectl apply -f databases\postgres-deployment-user.yaml
kubectl apply -f databases\postgres-deployment-userEvent.yaml
kubectl apply -f databases\postgres-deployment-loc.yaml
kubectl apply -f databases\postgres-deployment-pay.yaml
kubectl apply -f services\redis-deployment.yaml

echo.
echo Aplicando servicos e APIs...
kubectl apply -f services\eventservice-deployment.yaml
kubectl apply -f services\eventservice-service.yaml

kubectl apply -f services\locationservice-deployment.yaml
kubectl apply -f services\locationservice-service.yaml

kubectl apply -f services\paymentservice-deployment.yaml
kubectl apply -f services\paymentservice-service.yaml

kubectl apply -f services\userineventservice-deployment.yaml
kubectl apply -f services\userineventservice-service.yaml

kubectl apply -f services\userservice-deployment.yaml
kubectl apply -f services\userservice-service.yaml

:: HPA (se aplicável)
IF EXIST services\userservice-hpa.yaml (
    kubectl apply -f services\userservice-hpa.yaml
)

echo.
echo Criando Secret TLS do NGINX...
kubectl create secret tls nginx-tls-secret --cert=nginx/tls.crt --key=nginx/tls.key --dry-run=client -o yaml | kubectl apply -f -

echo.
echo Aplicando configuracoes do NGINX...
kubectl apply -f nginx\nginx-main-config.yaml
kubectl apply -f nginx\nginx-configmap.yaml
kubectl apply -f nginx\nginx-deployment.yaml
kubectl apply -f nginx\nginx-service.yaml

echo.
echo Tudo foi aplicado com sucesso!
pause
