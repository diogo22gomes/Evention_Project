# 🚀 EVENTIONAPI - Kubernetes + Minikube

Este projeto coloca os microserviços e bases de dados do sistema **EVENTIONAPI** a correr num cluster Kubernetes local com o **Minikube**. Inclui serviços, bases de dados PostgreSQL, Redis e o balanceador de carga **NGINX**.

---

## 🧱 Estrutura do Projeto

```
KubernetesRun/
│
├── databases/
│   ├── postgres-deployment-*.yaml
│   ├── postgres-storage-*.yaml
│   ├── redis-deployment.yaml
│   └── redis-storage.yaml
│
├── nginx/
│   ├── nginx-configmap.yaml
│   ├── nginx-deployment.yaml
│   ├── nginx-main-config.yaml
│   └── nginx-service.yaml
│
├── services/
│   ├── eventservice-*.yaml
│   ├── locationservice-*.yaml
│   ├── paymentservice-*.yaml
│   ├── userservice-*.yaml
│   └── usereventservice-*.yaml
│
├── start.sh       # Script de arranque para Mac/Linux/WSL
└── start.bat      # Script de arranque para Windows
```

---

## 🚦 Requisitos

Antes de começar, assegura-te de que tens os seguintes componentes instalados:

- [x] Docker
- [x] Minikube (`brew install minikube` no macOS ou [ver instruções aqui](https://minikube.sigs.k8s.io/docs/start/))
- [x] Kubectl
- [x] Bash (podes usar Git Bash no Windows)

---

## ▶️ Como arrancar o projeto

### 💻 macOS / WSL / Git Bash

```bash
chmod +x start.sh
./start.sh
```

### 🪟 Windows (linha de comandos ou duplo clique)

```cmd
start.bat
```

---

## 🔄 Reiniciar o cluster (reset)

Se precisares de limpar o cluster completamente antes de voltar a levantar os serviços:

```bash
kubectl delete all --all
kubectl delete pvc --all
```

---

## 📡 Verificação dos serviços

Para confirmar se tudo está a correr corretamente:

```bash
kubectl get pods -A
kubectl get svc -A
```

---

## 🌐 Acesso ao serviço via NGINX

Para abrir automaticamente no browser o serviço exposto via NGINX:

```bash
minikube service nginx-service
```

Este comando abrirá o navegador no endereço IP e porta atribuídos pelo Minikube, permitindo aceder aos microserviços.

---

## ℹ️ Notas finais

- Os ficheiros `.yaml` encontram-se organizados por categoria (bases de dados, serviços, nginx).
- Podes adicionar ou editar serviços conforme a arquitetura do sistema evolui.
- Garante que tens recursos suficientes alocados no Minikube (RAM e CPUs) para evitar falhas no arranque dos pods.

---


