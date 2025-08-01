#!/bin/bash

echo "🚀 Iniciando o Minikube..."
minikube start --driver=docker

echo ""
echo "📦 Aplicando os arquivos da pasta databases..."

kubectl apply -f databases/

echo ""
echo "🧩 Aplicando os serviços e APIs..."
kubectl apply -f services/

echo ""
echo "🔐 Criando Secret TLS para o NGINX..."
kubectl create secret tls nginx-tls-secret \
  --cert=nginx/tls.crt \
  --key=nginx/tls.key \
  --dry-run=client -o yaml | kubectl apply -f -

echo ""
echo "🌐 Aplicando a configuração do NGINX..."
kubectl apply -f nginx/

echo ""
echo "✅ Finalizado!"
kubectl get pods -A

echo "🚀 Expondo Portas..."
minikube tunnel
