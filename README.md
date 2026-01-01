# Library Management System (LMS)

**Library Management System (LMS)** is a secure, modular web application built with **Spring Boot** and **Spring Security**, designed for library administrators to efficiently manage books, students, publications, and subscriptions. This project follows modern software engineering practices, including layered architecture, RESTful principles, and authentication best practices.

---

## Project Overview

The LMS provides a centralized platform to perform administrative operations with security and auditability in mind. It facilitates:

- Book inventory and circulation management
- Student record tracking
- Vendor and publication management
- Subscription and reporting management
- Secure profile and credential management

The system is built to support **single-user administrative workflows**, with future extensibility for role-based access if required.

---

## Key Features

- **Authentication & Security**
  - Secure login using **Spring Security**.
  - Session-less authentication context with `UsernamePasswordAuthenticationToken`.
  - Encrypted password storage with **BCryptPasswordEncoder**.
  - Built-in protection against unauthorized access.

- **Administrative Dashboard**
  - Real-time operational metrics for books, students, and vendors.
  - Summary analytics for issued, available, and overdue books.
  - Daily quotes and system notifications.

- **Data Management**
  - CRUD operations for books, students, vendors, and publications.
  - Password reset and profile update functionality.
  - Audit-ready record management for administrative operations.

- **Web Interface**
  - Frontend built with **Thymeleaf** for server-side rendering.
  - Responsive design compatible with modern devices.
  - Dynamic UI elements like password toggle, alerts, and form validation.

- **Extensibility**
  - Modular services for user management, reporting, and operational workflows.
  - Prepared for future multi-role access and REST API integration.

---

## Architecture & Design

- **Layered Architecture**
  - **Controller Layer** – Handles HTTP requests and session management.
  - **Service Layer** – Encapsulates business logic for users, books, and reports.
  - **Repository Layer** – Data persistence using **Spring Data JPA** with **MySQL**.
  - **Security Layer** – Spring Security handles authentication and authorization.

- **Security & Session Management**
  - Authentication via `UsernamePasswordAuthenticationToken`.
  - Role-agnostic security (single-user administration).
  - Stateless session context maintained by Spring Security.

- **Persistence**
  - Relational database schema using **MySQL**.
  - Entities mapped with JPA/Hibernate annotations.
  - Repository pattern for clean and testable database access.

---

## Technology Stack

| Layer                 | Technology                                  |
|-----------------------|---------------------------------------------|
| Backend               | Java, Spring Boot, Spring Security          |
| Persistence           | Spring Data JPA, Hibernate, MySQL           |
| Frontend              | Thymeleaf, HTML5, CSS3, JavaScript         |
| Build & Dependency    | Maven                                       |
| Security              | Spring Security, BCryptPasswordEncoder      |
| Session Management    | Spring Security Context, UsernamePasswordAuthenticationToken |

---

## System Scope

This system is intended for **administrative users** in a library environment. It provides:

- End-to-end management of library operations.
- Secure and scalable architecture.
- Modular design for maintainability and future feature integration.
- Operational metrics for informed decision-making.

---

## Future Enhancements

- Role-based access control for multi-level administration.
- RESTful API endpoints for integration with external services.
- Frontend modernization with React or Angular.
- Advanced analytics and reporting dashboards.
