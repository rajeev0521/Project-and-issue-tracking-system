# Project & Issue Tracking System

A backend-driven project and issue tracking system inspired by Jira, built with Java and Spring Boot.

---

## Overview

The Project & Issue Tracking System is a RESTful backend application designed to help software development teams manage projects, track issues, and coordinate collaboration efficiently. The system focuses on clean architecture, security best practices, and scalable design patterns.

---

## Features

- **User Authentication** – Secure registration and login with JWT-based authentication
- **Role-Based Access Control** – Admin and Member roles with defined permissions
- **Project Management** – Create, update, and organize projects
- **Issue Tracking** – Full issue lifecycle management with assignment capabilities
- **Workflow Management** – Configurable issue status transitions (Open → In Progress → Done)
- **API Documentation** – Swagger/OpenAPI integration for API exploration

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | JPA (Hibernate) |
| Build Tool | Maven |
| API Docs | Swagger / OpenAPI |

---

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 14 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/rajeev0521/Project-and-issue-tracking-system.git
   cd Project-and-issue-tracking-system
   ```

2. **Configure the database**

   Update `src/main/resources/application.yml` with your PostgreSQL credentials:

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/tracking_db
       username: your_username
       password: your_password
   ```

3. **Build the project**

   ```bash
   mvn clean install
   ```

4. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

5. **Access the API documentation**

   Open `http://localhost:8080/swagger-ui.html` in your browser.

---

## API Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | User login |
| GET | `/api/projects` | List all projects |
| POST | `/api/projects` | Create a new project |
| GET | `/api/projects/{id}/issues` | List issues in a project |
| POST | `/api/projects/{id}/issues` | Create a new issue |

For complete API documentation, refer to the Swagger UI after starting the application.

---

## Project Structure

```
src/
├── main/
│   ├── java/com/trackingsystem/
│   │   ├── config/          # Security and application configuration
│   │   ├── auth/            # Authentication module
│   │   ├── user/            # User management module
│   │   ├── project/         # Project management module
│   │   ├── issue/           # Issue tracking module
│   │   └── common/          # Shared utilities and exceptions
│   └── resources/
│       └── application.yml  # Application configuration
└── test/                    # Unit and integration tests
```

---

## Documentation

- [Software Requirements Specification (SRS)](doc/ref/SRS1.md)
- [Project Report](doc/ref/project_report.md)

---

## Development Phases

| Phase | Description | Status |
|-------|-------------|--------|
| Phase 1 | Core prototype with authentication and basic CRUD | 🔄 In Progress |
| Phase 2 | Structured project & issue management | ⏳ Planned |
| Phase 3 | Workflow engine & business rules | ⏳ Planned |
| Phase 4 | Audit logs & activity timeline | ⏳ Planned |
| Phase 5 | Enhancements & optimization | ⏳ Planned |

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## License

This project is developed for educational purposes.

---

## Author

**Rajeev Gupta**
**Junaid Khan**

**MD Kasim**

**Md Mufti Habib**
---
