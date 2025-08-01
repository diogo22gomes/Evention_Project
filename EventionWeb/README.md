# Evention

**Evention** é uma aplicação web com dashboard moderno para a **gestão geral de eventos**. Com uma interface intuitiva, permite visualizar e acompanhar os eventos de forma eficiente, centralizada e em tempo real.

---

## Funcionalidades

- Gestão de eventos
- Visualização em tempo real do calendário de eventos
- Painel administrativo com estatísticas e métricas
- Interface responsiva e moderna

---

## Tecnologias

- [React](https://reactjs.org/)
- [Vite]
- [TailwindCSS / Styled Components / CSS Modules]

---

## Instalação

```bash
# Instala as dependências
npm install

# Compila o dashboard
npm run dev
```

## Run Production
```bash

docker build -t evention .

docker run -d -p 5173:80 evention
```