# 📚 Library Management System

![Java](https://img.shields.io/badge/Java-17-blue?style=flat&logo=java)
![Spring](https://img.shields.io/badge/Spring-6DB33F?logo=spring&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green?style=flat&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Dockerized-blue?style=flat&logo=postgresql)
![H2](https://img.shields.io/badge/H2_DB-0066CC?logo=databricks&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
![React](https://img.shields.io/badge/React-TypeScript-61DAFB?style=flat&logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=white)
![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen?style=flat&logo=testing-library)
![Status](https://img.shields.io/badge/Project-Complete-brightgreen)

A full-stack **Library Management System** designed to demonstrate backend Java Spring Boot skills and React frontend proficiency. It supports book loaning, book & user management, and RESTful operations, built with a microservice-ready, Dockerized environment.

---

## 🚀 Features

### 🧠 Backend
- **Spring Boot**, **JPA**, **Hibernate**, **PostgreSQL** (Docker) and **H2** (local dev)
- RESTful APIs for **Books**, **Authors**, **Users**, **Loans**, **Cart**, **Images**
- Role-based users, default users created at startup 
- Dockerfile, `docker-compose.yml`, and GitHub Actions integrated
- Extensive unit and integration testing across multiple layers (repository, service, controller)

### 🎨 Frontend
- **React + TypeScript**
- User management UI: create, update, delete, and view users
- Navigation between homepage, books page, and user management page
- Axios-based communication with backend APIs
- Global CORS setup using @Configuration to allow cross-origin requests from the React frontend

---

### ⚙️ Setup & Run

# Backend
./mvnw spring-boot:run

# Frontend
cd frontend
npm install
npm run dev

---

### 🐳 Dockerized Full Stack

# Build and run full environment
docker-compose up --build

### 🔁 GitHub Actions

Continuous Integration is set up using docker-build.yml. This file builds the backend Docker image, runs tests, and validates the build on every push to the repository.

---

## 📝 License

This project is intended for demonstration purposes only as part of a personal portfolio.
Please do not reuse, modify, or redistribute the code without permission.

© 2025 Joshua Lee