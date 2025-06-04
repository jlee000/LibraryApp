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

A full-stack **Library Management System** designed to demonstrate backend Java Spring Boot skills and React frontend proficiency with TypeScript. It supports book loaning, book & user management, and RESTful operations, built with a microservice-ready, Dockerized environment. **Spring Security** is implemented with custom filters, role-based authentication, and JWT to ensure secure user interactions.

---

## 🚀 Features

### 🧠 Backend
- **Spring Boot**, **JPA**, **Hibernate**, **PostgreSQL** (Dockerized) and **H2** (local dev)
- **Spring Security**: 
  - **Custom Filter Chain**: Allows detailed control over request filtering and security validation.
  - **Custom User Authentication**: Includes custom login and password decryption logic for secure login.
  - **Database User Login**: Authentication of users via database-backed credentials.  
  - **JWT-based Authentication**: After successful login, users receive a JWT token that they include in future requests to authenticate API calls, stateless user authentication, ensuring secure access for client-side requests.
  - **Role-based Authorization**: Different user roles (e.g., MEMBER, STAFF) grant different levels of access to the system.
- RESTful APIs for **Books**, **Authors**, **Users**, **BookLoans**, **Cart**, **CartItem**, **Images**, **Homepage**
- Role-based users with defined access control (Admin, User) and default users created at startup 
- Dockerfile, `docker-compose.yml`, and GitHub Actions integrated for automated CI/CD pipeline.
- Extensive unit and integration testing across all layers (repository, service, controller).

### 🎨 Frontend
- **React + TypeScript**
- **JWT Integration**: Tokens are sent along with every request for secure user authentication in frontend-to-backend communication. 
- **User Management UI**: Allows creation, updating, deletion, and viewing of users based on role-based access control.
- **Book Access**: Users can view and loan books in the system. Admins have additional permissions for book management; adding, editing and deleting.
- **Cart Management**: Users can add books to their cart for loan, and return books to the library.
- **User Roles**: The app uses role-based management for **Internal Users**, ensuring they have access to the appropriate features such as managing users and books.
- **Navigation**: Users can navigate between the homepage, books, account page, cart, book loans, internal user management and internal book management. Users must login to use most features.
- Global CORS setup using `@Configuration` to allow cross-origin requests from the React frontend.

---

### ⚙️ Setup & Run

# Backend
- ./mvnw spring-boot:run

# Frontend
- cd frontend
- npm install
- npm run dev

---

### 🐳 Dockerized Full Stack

# Build and run full environment
docker-compose up --build

### 🔁 GitHub Actions

Continuous Integration is set up using docker-build.yml. This file builds the backend Docker image, runs tests, and validates the build on every push to the repository.

---

## 🎯 Future Enhancements
- Expand JWT refresh token mechanism for long-term sessions.
- Introduce **OAuth2** for third-party authentication (Google, Facebook).
- Add **Payment Integration** for handling book loan transactions.

---

## 📝 License

This project is intended for demonstration purposes only as part of a personal portfolio.
Please do not reuse, modify, or redistribute the code without permission.

© 2025 Joshua Lee