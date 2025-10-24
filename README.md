# User Management Spring Project

## Description

This project is a **User Management system** built using **Spring Core, Spring Data JPA, and Spring MVC**. It allows CRUD operations for users, including **soft deletion**, with proper validations and role-based protection.

The application runs in **Docker** with **PostgreSQL** as the database and **Tomcat** as the servlet container.

---

## Features

* Create, Read, Update, and Delete (soft delete) users.
* Role-based restriction (Admin cannot be deleted).
* Input validation using **Jakarta Validation**.
* RESTful APIs for user management.
* Proper exception handling with custom error messages.
* Paginated listing of users.

---

## Configuration Steps

### 1. Spring Core

* **IoC & Dependency Injection**:

    * Beans are configured using `@Configuration` classes (`PersistenceConfig`, `WebConfig`).
    * `@ComponentScan` is used to auto-detect services, controllers, and repositories.
* **Bean Management**:

    * All beans like `DataSource`, `EntityManagerFactory`, and `TransactionManager` are managed by Spring.
    * Beans are injected via constructor injection.

---

### 2. Spring Data JPA

* **Persistence**:

    * Entities are mapped using `@Entity` annotations (`User` entity).
    * The repository layer uses **Spring Data JPA** (`UserRepository`) for database operations.
* **Transactions**:

    * `@EnableTransactionManagement` enables declarative transaction management.
    * `JpaTransactionManager` ensures transactional consistency for CRUD operations.

---

### 3. Spring MVC

* **Controllers**:

    * `UserController` handles RESTful endpoints for user management (`/api/users`).
    * `HelloController` provides a simple `/hello` endpoint for testing.
* **DispatcherServlet Configuration**:

    * Configured programmatically via `WebAppInitializer` (`AbstractAnnotationConfigDispatcherServletInitializer`).
    * `WebConfig` sets up component scanning for controllers and message converters (`MappingJackson2HttpMessageConverter`).
* **Endpoints**:

    * `POST /api/users` → Create user
    * `GET /api/users` → List users (supports pagination)
    * `GET /api/users/{id}` → Get user by ID
    * `PUT /api/users/{id}` → Update user
    * `DELETE /api/users/{id}` → Soft delete user

---

## Running the Project

### 1. Docker Setup

The project uses Docker Compose to run **PostgreSQL, PgAdmin, and Tomcat**:

```yaml
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: userdb
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
    ports:
      - "5432:5432"

  pgadmin:
    image: dpage/pgadmin4:7.8
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "5050:80"
    depends_on:
      - postgres

  tomcat:
    build: .
    ports:
      - "8081:8080"
    depends_on:
      - postgres
```

### 2. Build & Run

```bash
# Build the WAR file
mvn clean package

# Start Docker containers
docker-compose up --build
```

* Access the API at: `http://localhost:8081/api/users`
* Access Tomcat default page at: `http://localhost:8081/`
* Access PgAdmin at: `http://localhost:5050/`

---

## Notes

* The `PersistenceConfig` points to PostgreSQL running inside Docker (`jdbc:postgresql://postgres:5432/userdb`).
* The admin user cannot be deleted for security reasons.
* All entities use proper validation annotations to ensure data integrity.
* Soft deletion marks `active=false` without physically deleting the record.

---

## Technologies Used

* Java 17
* Spring Core, Spring MVC, Spring Data JPA
* PostgreSQL
* Docker & Docker Compose
* Tomcat 10
* Maven
