# Etapa 1: Build da aplicação
FROM node:20-alpine AS build

WORKDIR /app

# Copiar apenas os ficheiros de dependências primeiro para aproveitar cache
COPY package*.json ./

RUN npm install

# Copiar o resto do código
COPY . .

# Compilar a aplicação (gera o diretório dist)
RUN npm run build

# Etapa 2: Imagem final de produção com Nginx
FROM nginx:stable-alpine

# Copiar os ficheiros gerados no build para o Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Substituir a configuração padrão do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expor a porta 80
EXPOSE 80

# Comando padrão para manter o Nginx em execução
CMD ["nginx", "-g", "daemon off;"]
