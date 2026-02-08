# Software Requirements Specification (SRS)

## Project & Issue Tracking System - Phase 1: Prototype

---

**Document Version:** 1.0  
**Date:** 09 February 2026  
**Prepared By:** Rajeev Gupta  
**Status:** Draft  

---

## Table of Contents

1. [Introduction](#1-introduction)
   - 1.1 [Purpose](#11-purpose)
   - 1.2 [Scope](#12-scope)
   - 1.3 [Definitions, Acronyms, and Abbreviations](#13-definitions-acronyms-and-abbreviations)
   - 1.4 [References](#14-references)
   - 1.5 [Overview](#15-overview)
2. [Overall Description](#2-overall-description)
   - 2.1 [Product Perspective](#21-product-perspective)
   - 2.2 [Product Functions](#22-product-functions)
   - 2.3 [User Classes and Characteristics](#23-user-classes-and-characteristics)
   - 2.4 [Operating Environment](#24-operating-environment)
   - 2.5 [Design and Implementation Constraints](#25-design-and-implementation-constraints)
   - 2.6 [Assumptions and Dependencies](#26-assumptions-and-dependencies)
3. [Specific Requirements](#3-specific-requirements)
   - 3.1 [Functional Requirements](#31-functional-requirements)
   - 3.2 [Non-Functional Requirements](#32-non-functional-requirements)
   - 3.3 [Interface Requirements](#33-interface-requirements)
4. [Data Requirements](#4-data-requirements)
   - 4.1 [Logical Data Model](#41-logical-data-model)
   - 4.2 [Data Dictionary](#42-data-dictionary)
5. [Appendix](#5-appendix)

---

## 1. Introduction

### 1.1 Purpose

This Software Requirements Specification (SRS) document provides a comprehensive description of the requirements for **Phase 1 (Prototype)** of the Project & Issue Tracking System. This document is intended for:

- Development team members
- Project stakeholders
- Quality assurance personnel
- System architects

The purpose of this document is to clearly define the functional and non-functional requirements that must be met to successfully deliver a minimal but fully functional issue tracking system.

### 1.2 Scope

**Phase 1: Prototype** aims to build a core working system that demonstrates the fundamental capabilities of an issue tracking platform. This phase focuses on establishing the foundational backend infrastructure with:

- User authentication and authorization
- Basic project management
- Core issue tracking functionality
- RESTful API architecture

**In Scope for Phase 1:**

| Feature | Description |
|---------|-------------|
| User Registration | New user account creation |
| User Authentication | Secure login with JWT tokens |
| Role-Based Access | Admin and Member roles |
| Project Management | Create and manage projects |
| Issue Management | Create, assign, and track issues |
| Basic Workflow | Open → In Progress → Done status flow |

**Out of Scope for Phase 1:**

- Frontend user interface
- Advanced workflow customization
- Issue comments and discussions
- Audit logging
- Email notifications
- Third-party integrations

### 1.3 Definitions, Acronyms, and Abbreviations

| Term | Definition |
|------|------------|
| **API** | Application Programming Interface |
| **CRUD** | Create, Read, Update, Delete operations |
| **JWT** | JSON Web Token - A standard for secure authentication |
| **RBAC** | Role-Based Access Control |
| **REST** | Representational State Transfer |
| **SRS** | Software Requirements Specification |
| **ORM** | Object-Relational Mapping |
| **JPA** | Java Persistence API |
| **DTO** | Data Transfer Object |

### 1.4 References

| Document | Description |
|----------|-------------|
| Project Report | Project & Issue Tracking System - Project Report v1.0 |
| IEEE 830-1998 | IEEE Recommended Practice for Software Requirements Specifications |
| Spring Boot Documentation | <https://docs.spring.io/spring-boot/docs/current/reference/html/> |
| JWT Specification | RFC 7519 - JSON Web Token |

### 1.5 Overview

The remainder of this document is organized as follows:

- **Section 2** provides an overall description of the product
- **Section 3** details the specific functional and non-functional requirements
- **Section 4** describes the data requirements and logical data model
- **Section 5** contains appendices with additional information

---

## 2. Overall Description

### 2.1 Product Perspective

The Project & Issue Tracking System is a standalone backend application designed to manage software development projects and track issues. It is inspired by industry-standard tools like Jira but focuses on simplicity and learning.

**System Context Diagram:**

```
┌─────────────────────────────────────────────────────────────┐
│                    External Systems                          │
│                                                              │
│   ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    │
│   │  REST API   │    │   Swagger   │    │  Database   │    │
│   │   Clients   │    │     UI      │    │ (PostgreSQL)│    │
│   └──────┬──────┘    └──────┬──────┘    └──────┬──────┘    │
│          │                  │                   │           │
│          └────────────┬─────┴───────────────────┘           │
│                       ▼                                      │
│   ┌─────────────────────────────────────────────────────┐   │
│   │         Project & Issue Tracking System              │   │
│   │                 (Spring Boot Backend)                │   │
│   └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Product Functions

Phase 1 of the system provides the following high-level functions:

| Function Category | Description |
|-------------------|-------------|
| **Authentication** | User registration, login, logout, and token management |
| **User Management** | Profile management and role assignments |
| **Project Management** | Create, view, update, and delete projects |
| **Issue Management** | Create, view, update, delete, and assign issues |
| **Status Management** | Track issue status through defined workflow states |

### 2.3 User Classes and Characteristics

| User Class | Description | Privileges |
|------------|-------------|------------|
| **Admin** | System administrator with full access | - Manage all users<br>- Create/delete projects<br>- Manage all issues<br>- Assign roles to users |
| **Member** | Regular team member with project access | - View assigned projects<br>- Create and manage own issues<br>- Update issue status<br>- View project issues |
| **Anonymous** | Unauthenticated user | - Register new account<br>- Login to system |

### 2.4 Operating Environment

| Component | Specification |
|-----------|---------------|
| **Server OS** | Linux/Windows/macOS |
| **Runtime** | Java 17+ (JDK) |
| **Database** | PostgreSQL 14+ |
| **Memory** | Minimum 512MB RAM |
| **Storage** | Minimum 1GB disk space |

### 2.5 Design and Implementation Constraints

| Constraint | Description |
|------------|-------------|
| **Technology Stack** | Must use Java 17 and Spring Boot framework |
| **Database** | PostgreSQL is the required database system |
| **Authentication** | JWT-based authentication is mandatory |
| **API Standard** | All APIs must follow RESTful principles |
| **Documentation** | APIs must be documented using Swagger/OpenAPI |
| **Build Tool** | Maven must be used for dependency management |

### 2.6 Assumptions and Dependencies

**Assumptions:**

1. Users have access to a REST API client (Postman, cURL, etc.) for testing
2. PostgreSQL database server is available and accessible
3. Java 17 runtime environment is installed on the development machine
4. Development team is familiar with Spring Boot framework

**Dependencies:**

| Dependency | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 3.x | Application framework |
| Spring Security | 6.x | Security framework |
| Spring Data JPA | 3.x | Database abstraction |
| PostgreSQL Driver | 42.x | Database connectivity |
| JJWT | 0.11.x | JWT token handling |
| Lombok | 1.18.x | Boilerplate reduction |
| Swagger/OpenAPI | 3.x | API documentation |

---

## 3. Specific Requirements

### 3.1 Functional Requirements

#### 3.1.1 User Registration Module

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| FR-REG-001 | The system shall allow new users to register by providing username, email, and password | High |
| FR-REG-002 | The system shall validate that the email address is in a valid format | High |
| FR-REG-003 | The system shall validate that the username is unique | High |
| FR-REG-004 | The system shall validate that the email is unique | High |
| FR-REG-005 | The system shall enforce a minimum password length of 8 characters | High |
| FR-REG-006 | The system shall store passwords in an encrypted format using BCrypt | Optional for now |
| FR-REG-007 | The system shall assign the "Member" role to newly registered users by default | Medium |
| FR-REG-008 | The system shall return appropriate error messages for validation failures | High |

**API Endpoint:**

```
POST /api/auth/register
```

**Request Body:**

```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "fullName": "string"
}
```

**Response:**

```json
{
  "id": "uuid",
  "username": "string",
  "email": "string",
  "fullName": "string",
  "role": "MEMBER",
  "createdAt": "timestamp"
}
```

---

#### 3.1.2 User Authentication Module

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| FR-AUTH-001 | The system shall allow registered users to login using email and password | High |
| FR-AUTH-002 | The system shall generate a JWT token upon successful authentication | High |
| FR-AUTH-003 | The JWT token shall have a configurable expiration time (default: 24 hours) | High |
| FR-AUTH-004 | The system shall return a 401 Unauthorized response for invalid credentials | High |
| FR-AUTH-005 | The system shall validate JWT tokens on all protected endpoints | High |
| FR-AUTH-006 | The system shall return a 403 Forbidden response for expired tokens | High |
| FR-AUTH-007 | The system shall support token refresh functionality | Medium |

**API Endpoints:**

```
POST /api/auth/login
POST /api/auth/refresh
```

**Login Request:**

```json
{
  "email": "string",
  "password": "string"
}
```

**Login Response:**

```json
{
  "accessToken": "string",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": "uuid",
    "username": "string",
    "email": "string",
    "role": "string"
  }
}
```

---

#### 3.1.3 Role-Based Access Control

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| FR-RBAC-001 | The system shall support two roles: Admin and Member | High |
| FR-RBAC-002 | Admin users shall have access to all system resources | High |
| FR-RBAC-003 | Member users shall only access projects they are assigned to | High |
| FR-RBAC-004 | Only Admin users shall be able to assign roles to other users | High |
| FR-RBAC-005 | Only Admin users shall be able to create and delete projects | High |
| FR-RBAC-006 | The system shall enforce role-based permissions on all protected endpoints | High |

**Role Permissions Matrix:**

| Resource | Action | Admin | Member |
|----------|--------|-------|--------|
| Users | View All | ✅ | ❌ |
| Users | Update Role | ✅ | ❌ |
| Projects | Create | ✅ | ❌ |
| Projects | Delete | ✅ | ❌ |
| Projects | View All | ✅ | ❌ |
| Projects | View Assigned | ✅ | ✅ |
| Projects | Update | ✅ | ❌ |
| Issues | Create | ✅ | ✅ |
| Issues | View | ✅ | ✅ (assigned projects) |
| Issues | Update | ✅ | ✅ (own/assigned) |
| Issues | Delete | ✅ | ❌ |
| Issues | Assign | ✅ | ❌ |

---

#### 3.1.4 Project Management Module

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| FR-PROJ-001 | Admin users shall be able to create new projects | High |
| FR-PROJ-002 | Each project shall have a unique project key (e.g., PROJ-001) | High |
| FR-PROJ-003 | The system shall store project name, description, and key | High |
| FR-PROJ-004 | Admin users shall be able to view all projects | High |
| FR-PROJ-005 | Member users shall only view projects they are assigned to | High |
| FR-PROJ-006 | Admin users shall be able to update project details | High |
| FR-PROJ-007 | Admin users shall be able to delete projects | High |
| FR-PROJ-008 | Deleting a project shall also delete all associated issues | Medium |
| FR-PROJ-009 | The system shall track project creation date and last updated date | Medium |

**API Endpoints:**

```
POST   /api/projects              - Create project (Admin)
GET    /api/projects              - List all projects (Admin) / assigned projects (Member)
GET    /api/projects/{id}         - Get project by ID
PUT    /api/projects/{id}         - Update project (Admin)
DELETE /api/projects/{id}         - Delete project (Admin)
```

**Project Entity:**

```json
{
  "id": "uuid",
  "key": "string (e.g., PROJ)",
  "name": "string",
  "description": "string",
  "createdBy": "uuid",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

---

#### 3.1.5 Issue Management Module

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| FR-ISSUE-001 | Authenticated users shall be able to create issues within their assigned projects | High |
| FR-ISSUE-002 | Each issue shall have a unique issue key (e.g., PROJ-1, PROJ-2) | High |
| FR-ISSUE-003 | The system shall store issue title, description, status, and assignee | High |
| FR-ISSUE-004 | Issues shall be created with a default status of "OPEN" | High |
| FR-ISSUE-005 | Users shall be able to view all issues within their assigned projects | High |
| FR-ISSUE-006 | Users shall be able to update issues they created or are assigned to | High |
| FR-ISSUE-007 | Admin users shall be able to delete any issue | High |
| FR-ISSUE-008 | Admin users shall be able to assign issues to project members | High |
| FR-ISSUE-009 | The system shall track issue creation date and last updated date | Medium |
| FR-ISSUE-010 | Users shall be able to filter issues by status | Medium |
| FR-ISSUE-011 | Users shall be able to filter issues by assignee | Medium |

**API Endpoints:**

```
POST   /api/projects/{projectId}/issues           - Create issue
GET    /api/projects/{projectId}/issues           - List project issues
GET    /api/projects/{projectId}/issues/{id}      - Get issue by ID
PUT    /api/projects/{projectId}/issues/{id}      - Update issue
DELETE /api/projects/{projectId}/issues/{id}      - Delete issue (Admin)
PATCH  /api/projects/{projectId}/issues/{id}/status - Update issue status
PATCH  /api/projects/{projectId}/issues/{id}/assign - Assign issue (Admin)
```

**Issue Entity:**

```json
{
  "id": "uuid",
  "key": "string (e.g., PROJ-1)",
  "title": "string",
  "description": "string",
  "status": "OPEN | IN_PROGRESS | DONE",
  "projectId": "uuid",
  "reporterId": "uuid",
  "assigneeId": "uuid (nullable)",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

---

#### 3.1.6 Issue Status Workflow

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| FR-WF-001 | The system shall support three issue statuses: OPEN, IN_PROGRESS, DONE | High |
| FR-WF-002 | New issues shall be created with status OPEN | High |
| FR-WF-003 | Issues can transition from OPEN to IN_PROGRESS | High |
| FR-WF-004 | Issues can transition from IN_PROGRESS to DONE | High |
| FR-WF-005 | Issues can transition from IN_PROGRESS back to OPEN | Medium |
| FR-WF-006 | Issues can transition from DONE back to IN_PROGRESS (reopen) | Medium |
| FR-WF-007 | The system shall reject invalid status transitions | High |

**Status Transition Diagram:**

```
┌──────────────────────────────────────────────────────────┐
│                                                          │
│     ┌────────┐      ┌─────────────┐      ┌────────┐     │
│     │  OPEN  │─────►│ IN_PROGRESS │─────►│  DONE  │     │
│     └────────┘      └─────────────┘      └────────┘     │
│          ▲                │                    │         │
│          │                │                    │         │
│          └────────────────┘                    │         │
│                                                │         │
│          ▲                                     │         │
│          │               ┌─────────────────────┘         │
│          │               │   (Reopen)                    │
│          │               ▼                               │
│          │         ┌─────────────┐                       │
│          └─────────│ IN_PROGRESS │                       │
│                    └─────────────┘                       │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

**Valid Transitions:**

| From Status | To Status | Description |
|-------------|-----------|-------------|
| OPEN | IN_PROGRESS | Start work on issue |
| IN_PROGRESS | DONE | Complete issue |
| IN_PROGRESS | OPEN | Return to backlog |
| DONE | IN_PROGRESS | Reopen issue |

---

### 3.2 Non-Functional Requirements

#### 3.2.1 Performance Requirements

| Req ID | Requirement | Target |
|--------|-------------|--------|
| NFR-PERF-001 | API response time for simple queries | < 200ms |
| NFR-PERF-002 | API response time for complex queries | < 500ms |
| NFR-PERF-003 | System shall support concurrent users | Min 50 users |
| NFR-PERF-004 | Database query optimization | Indexed queries |

#### 3.2.2 Security Requirements

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| NFR-SEC-001 | All passwords shall be hashed using BCrypt algorithm | High |
| NFR-SEC-002 | JWT tokens shall use HS256 or RS256 signing algorithm | High |
| NFR-SEC-003 | All API endpoints (except auth) shall require valid JWT token | High |
| NFR-SEC-004 | SQL injection prevention through parameterized queries | High |
| NFR-SEC-005 | Input validation on all user-supplied data | High |
| NFR-SEC-006 | CORS configuration for allowed origins | Medium |

#### 3.2.3 Reliability Requirements

| Req ID | Requirement | Target |
|--------|-------------|--------|
| NFR-REL-001 | System availability | 99% uptime |
| NFR-REL-002 | Data persistence | All data persisted to PostgreSQL |
| NFR-REL-003 | Error handling | Graceful error responses with proper HTTP codes |

#### 3.2.4 Maintainability Requirements

| Req ID | Requirement | Description |
|--------|-------------|-------------|
| NFR-MAIN-001 | Code shall follow clean code principles | Readable, modular code |
| NFR-MAIN-002 | APIs shall be documented using Swagger/OpenAPI | Automated documentation |
| NFR-MAIN-003 | Code shall be organized in a layered architecture | Controller → Service → Repository |
| NFR-MAIN-004 | Proper logging shall be implemented | SLF4J/Logback |

#### 3.2.5 Scalability Requirements

| Req ID | Requirement | Description |
|--------|-------------|-------------|
| NFR-SCALE-001 | Stateless application design | No server-side sessions |
| NFR-SCALE-002 | Database connection pooling | HikariCP |
| NFR-SCALE-003 | Modular architecture | Feature-based module separation |

---

### 3.3 Interface Requirements

#### 3.3.1 API Interface

| Aspect | Specification |
|--------|---------------|
| Protocol | HTTP/HTTPS |
| Format | JSON |
| Authentication | Bearer Token (JWT) |
| Content-Type | application/json |
| Character Encoding | UTF-8 |

#### 3.3.2 API Response Format

**Success Response:**

```json
{
  "success": true,
  "data": { ... },
  "message": "Operation successful",
  "timestamp": "2026-02-09T00:00:00Z"
}
```

**Error Response:**

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable message",
    "details": [ ... ]
  },
  "timestamp": "2026-02-09T00:00:00Z"
}
```

#### 3.3.3 HTTP Status Codes

| Code | Description | Usage |
|------|-------------|-------|
| 200 | OK | Successful GET, PUT, PATCH |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Validation errors |
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource |
| 500 | Internal Server Error | Server error |

---

## 4. Data Requirements

### 4.1 Logical Data Model

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           Entity Relationship Diagram                   │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│   ┌──────────────┐         ┌──────────────┐         ┌──────────────┐   │
│   │    USER      │         │   PROJECT    │         │    ISSUE     │   │
│   ├──────────────┤         ├──────────────┤         ├──────────────┤   │
│   │ id (PK)      │         │ id (PK)      │         │ id (PK)      │   │
│   │ username     │◄───┐    │ key          │◄───┐    │ key          │   │
│   │ email        │    │    │ name         │    │    │ title        │   │
│   │ password     │    │    │ description  │    │    │ description  │   │
│   │ fullName     │    │    │ created_by   │────┘    │ status       │   │
│   │ role         │    │    │ created_at   │         │ project_id   │───┤
│   │ created_at   │    │    │ updated_at   │         │ reporter_id  │───┤
│   │ updated_at   │    │    └──────────────┘         │ assignee_id  │───┘
│   └──────────────┘    │                             │ created_at   │
│                       │                             │ updated_at   │
│                       │                             └──────────────┘
│                       │                                    │
│                       └────────────────────────────────────┘
│                                                                         │
│   ┌──────────────────────────────────────────────────────────────────┐ │
│   │                    PROJECT_MEMBERS (Join Table)                   │ │
│   ├──────────────────────────────────────────────────────────────────┤ │
│   │ project_id (FK) ←──────────────────────────────► user_id (FK)    │ │
│   │ joined_at                                                         │ │
│   └──────────────────────────────────────────────────────────────────┘ │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### 4.2 Data Dictionary

#### 4.2.1 User Entity

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Unique identifier |
| username | VARCHAR(50) | UNIQUE, NOT NULL | User's username |
| email | VARCHAR(100) | UNIQUE, NOT NULL | User's email address |
| password | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| full_name | VARCHAR(100) | NOT NULL | User's full name |
| role | ENUM | NOT NULL, DEFAULT 'MEMBER' | ADMIN or MEMBER |
| created_at | TIMESTAMP | NOT NULL | Account creation time |
| updated_at | TIMESTAMP | NOT NULL | Last update time |

#### 4.2.2 Project Entity

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Unique identifier |
| key | VARCHAR(10) | UNIQUE, NOT NULL | Project key (e.g., PROJ) |
| name | VARCHAR(100) | NOT NULL | Project name |
| description | TEXT | - | Project description |
| created_by | UUID | FK → User.id, NOT NULL | Project creator |
| created_at | TIMESTAMP | NOT NULL | Creation time |
| updated_at | TIMESTAMP | NOT NULL | Last update time |

#### 4.2.3 Issue Entity

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Unique identifier |
| key | VARCHAR(20) | UNIQUE, NOT NULL | Issue key (e.g., PROJ-1) |
| title | VARCHAR(200) | NOT NULL | Issue title |
| description | TEXT | - | Issue description |
| status | ENUM | NOT NULL, DEFAULT 'OPEN' | OPEN, IN_PROGRESS, DONE |
| project_id | UUID | FK → Project.id, NOT NULL | Parent project |
| reporter_id | UUID | FK → User.id, NOT NULL | Issue creator |
| assignee_id | UUID | FK → User.id, NULLABLE | Assigned user |
| created_at | TIMESTAMP | NOT NULL | Creation time |
| updated_at | TIMESTAMP | NOT NULL | Last update time |

#### 4.2.4 Project Members Entity

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| project_id | UUID | FK → Project.id, NOT NULL | Project reference |
| user_id | UUID | FK → User.id, NOT NULL | User reference |
| joined_at | TIMESTAMP | NOT NULL | Membership start time |
| **PK** | - | (project_id, user_id) | Composite primary key |

---

## 5. Appendix

### 5.1 API Endpoints Summary

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | /api/auth/register | Register new user | Public |
| POST | /api/auth/login | User login | Public |
| POST | /api/auth/refresh | Refresh token | Authenticated |
| GET | /api/users/me | Get current user profile | Authenticated |
| PUT | /api/users/me | Update own profile | Authenticated |
| GET | /api/users | List all users | Admin |
| PUT | /api/users/{id}/role | Update user role | Admin |
| POST | /api/projects | Create project | Admin |
| GET | /api/projects | List projects | Authenticated |
| GET | /api/projects/{id} | Get project details | Authenticated |
| PUT | /api/projects/{id} | Update project | Admin |
| DELETE | /api/projects/{id} | Delete project | Admin |
| POST | /api/projects/{id}/members | Add project member | Admin |
| DELETE | /api/projects/{id}/members/{userId} | Remove project member | Admin |
| POST | /api/projects/{projectId}/issues | Create issue | Authenticated |
| GET | /api/projects/{projectId}/issues | List issues | Authenticated |
| GET | /api/projects/{projectId}/issues/{id} | Get issue | Authenticated |
| PUT | /api/projects/{projectId}/issues/{id} | Update issue | Authenticated |
| DELETE | /api/projects/{projectId}/issues/{id} | Delete issue | Admin |
| PATCH | /api/projects/{projectId}/issues/{id}/status | Update status | Authenticated |
| PATCH | /api/projects/{projectId}/issues/{id}/assign | Assign issue | Admin |

### 5.2 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/trackingsystem/
│   │       ├── TrackingSystemApplication.java
│   │       ├── config/
│   │       │   ├── SecurityConfig.java
│   │       │   ├── JwtConfig.java
│   │       │   └── SwaggerConfig.java
│   │       ├── auth/
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   ├── dto/
│   │       │   └── util/
│   │       ├── user/
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   ├── repository/
│   │       │   ├── entity/
│   │       │   └── dto/
│   │       ├── project/
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   ├── repository/
│   │       │   ├── entity/
│   │       │   └── dto/
│   │       ├── issue/
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   ├── repository/
│   │       │   ├── entity/
│   │       │   └── dto/
│   │       └── common/
│   │           ├── exception/
│   │           ├── response/
│   │           └── util/
│   └── resources/
│       ├── application.yml
│       └── application-dev.yml
└── test/
    └── java/
        └── com/trackingsystem/
            └── ...
```

### 5.3 Acceptance Criteria

| Feature | Acceptance Criteria |
|---------|---------------------|
| User Registration | User can register with valid credentials and receive confirmation |
| User Login | User can login and receive a valid JWT token |
| Token Validation | Protected endpoints reject requests without valid tokens |
| Role Enforcement | Admin-only endpoints reject Member role requests |
| Project CRUD | Admin can create, read, update, delete projects |
| Issue CRUD | Users can create, read, update issues in assigned projects |
| Status Transitions | Issues follow defined workflow state transitions |
| API Documentation | Swagger UI is accessible and shows all endpoints |

---

**Document Approval:**

| Role | Name | Signature | Date |
|------|------|-----------|------|
| Project Lead | | | |
| Technical Lead | | | |
| Quality Assurance | | | |

---

*End of Document*
