# 📝 A Java REST API for a Simple Personal Blog

This is the backend for a simple blog application, built with **Spring Boot**. It exposes a RESTful API that handles blog posts, tags, and authors. The frontend is built using **Angular** and communicates with this API.

---

## 🚀 Features

- Create, read, update, and delete blog posts
- Associate posts with multiple tags (many-to-many relationship)
- Link posts to authors (many-to-one relationship)
- Generate slugs automatically from post titles
- Automatically create and associate tags on post creation
- Securely expose selected user information in responses
- Ready for deployment on [Render](https://render.com)

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Data JPA
- PostgreSQL
- Angular (Frontend)
- Maven

---

## 📦 Endpoints Overview

### 📝 Posts

| Method | Endpoint           | Description                          |
|--------|--------------------|--------------------------------------|
| GET    | `/posts`           | Get all posts                        |
| GET    | `/posts/{slug}`    | Get a post by slug                   |
| POST   | `/posts`           | Create a new post                    |
| PUT    | `/posts`           | Update a post                        |
| DELETE | `/posts`           | Delete a post (send full post object)|

---

### 🏷️ Tags

| Method | Endpoint           | Description                          |
|--------|--------------------|--------------------------------------|
| GET    | `/tags`            | Get all tags                         |
| GET    | `/tags/{name}`     | Get a tag by name                    |
| POST   | `/tags`            | Create a new tag                     |
| DELETE | `/tags/{id}`       | Delete a tag by ID                   |

---

### 👤 Users

| Method | Endpoint                   | Description                          |
|--------|----------------------------|--------------------------------------|
| GET    | `/users`                   | Get all users                        |
| GET    | `/users/{id}`              | Get user by ID                       |
| GET    | `/users/email/{email}`     | Get user by email                    |
| GET    | `/users/username/{username}` | Get user by username               |
| POST   | `/users`                   | Create a new user                    |
| PUT    | `/users/{id}`              | Update a user                        |
| DELETE | `/users/{id}`              | Delete a user                        |

---

### ❤️ Health Check

| Method | Endpoint     | Description            |
|--------|--------------|------------------------|
| GET    | `/health`    | Returns `OK` if running|

---

Request/Response format uses DTOs for better control.

### Sample JSON (POST `/api/posts`)

```json
{
  "title": "Building a Career You Love",
  "content": "It takes time, effort, and alignment...",
  "published": true,
  "author": { "id": 1 },
  "tags": [
    { "name": "Career" },
    { "name": "Personal" }
  ]
}
```

---

## 🧪 Running Locally

### Prerequisites

- Java 17+
- Maven
- PostgreSQL running locally or via Docker

### 1. Clone the project

### 2. Configure environment variables

In `src/main/resources/application.properties`, reference your hidden environment variables like this:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
```

On **Render**, you can set these variables securely in the dashboard.

For local development, you can use a `.env` file with tools like [dotenv-spring](https://github.com/cdimascio/dotenv-java) or export them manually:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/blogdb
export DB_USER=your_user
export DB_PASS=your_password
```

### 3. Run the app

```bash
./mvnw spring-boot:run
```

Server will start at `http://localhost:8080`.

---

## 🌐 CORS Configuration

CORS is enabled in `WebConfig.java` to allow requests from your Angular frontend:

```java
.allowedOrigins("http://localhost:4200")
```

Make sure to update it for production.

---

## 📦 Deployment on Render

- Create a PostgreSQL instance
- Add environment variables in the Render dashboard:
    - `DB_URL`
    - `DB_USER`
    - `DB_PASS`
- Deploy as a Java service using:
    - Build Command: `./mvnw clean package`
    - Start Command: `java -jar target/*.jar`

---

## ✍️ Author

Developed by [**Marcos Capella**](https://marcoscapella.com.br) 
Feel free to connect: [LinkedIn](https://linkedin.com/in/marcoscapella)