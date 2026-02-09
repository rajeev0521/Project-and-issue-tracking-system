# Database Design Document

## Project & Issue Tracking System - Phase 1

**Version:** 1.0  
**Date:** 09 February 2026  
**Database:** PostgreSQL 14+

---

## 1. Entity Relationship Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              DATABASE SCHEMA                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                  │
│  ┌──────────────────┐                    ┌──────────────────┐                   │
│  │      users       │                    │     projects     │                   │
│  ├──────────────────┤                    ├──────────────────┤                   │
│  │ id (PK)          │◄───────────────────┤ created_by (FK)  │                   │
│  │ username         │                    │ id (PK)          │                   │
│  │ email            │                    │ project_key      │                   │
│  │ password_hash    │                    │ name             │                   │
│  │ full_name        │                    │ description      │                   │
│  │ role             │    ┌───────────────┤ created_at       │                   │
│  │ is_active        │    │               │ updated_at       │                   │
│  │ created_at       │    │               └──────────────────┘                   │
│  │ updated_at       │    │                        │                             │
│  └──────────────────┘    │                        │                             │
│           │              │                        │                             │
│           │              │                        │                             │
│           ▼              ▼                        ▼                             │
│  ┌────────────────────────────┐         ┌──────────────────┐                   │
│  │     project_members        │         │      issues      │                   │
│  ├────────────────────────────┤         ├──────────────────┤                   │
│  │ id (PK)                    │         │ id (PK)          │                   │
│  │ project_id (FK)            │◄────────┤ project_id (FK)  │                   │
│  │ user_id (FK)               │         │ issue_key        │                   │
│  │ role_in_project            │         │ title            │                   │
│  │ joined_at                  │         │ description      │                   │
│  └────────────────────────────┘         │ status           │                   │
│                                         │ priority (P2)    │                   │
│           ┌─────────────────────────────┤ issue_type (P2)  │                   │
│           │                             │ reporter_id (FK) │                   │
│           │         ┌───────────────────┤ assignee_id (FK) │                   │
│           ▼         ▼                   │ created_at       │                   │
│  ┌──────────────────┐                   │ updated_at       │                   │
│  │      users       │                   └──────────────────┘                   │
│  │  (reporter)      │                            │                             │
│  │  (assignee)      │                            │                             │
│  └──────────────────┘                            │                             │
│                                                  │                             │
│                              ┌───────────────────┘                             │
│                              ▼                                                  │
│                    ┌──────────────────┐    ┌──────────────────┐                │
│                    │ issue_comments   │    │   audit_logs     │                │
│                    │     (Phase 2)    │    │    (Phase 4)     │                │
│                    └──────────────────┘    └──────────────────┘                │
│                                                                                  │
│  Legend: (P2) = Phase 2, (P3) = Phase 3, (P4) = Phase 4                        │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Table Definitions

### 2.1 users

Stores all user accounts in the system.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| `username` | VARCHAR(50) | UNIQUE, NOT NULL | Login username |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL | User email address |
| `password_hash` | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| `full_name` | VARCHAR(100) | NOT NULL | Display name |
| `role` | VARCHAR(20) | NOT NULL, DEFAULT 'MEMBER' | System role (ADMIN, MEMBER) |
| `is_active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Account status |
| `avatar_url` | VARCHAR(500) | NULL | Profile picture (future) |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Account creation time |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Last modification time |

**Indexes:**

- `idx_users_email` on `email`
- `idx_users_username` on `username`
- `idx_users_role` on `role`

---

### 2.2 projects

Stores project information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| `project_key` | VARCHAR(10) | UNIQUE, NOT NULL | Short project code (e.g., PROJ) |
| `name` | VARCHAR(100) | NOT NULL | Project name |
| `description` | TEXT | NULL | Project description |
| `created_by` | UUID | FOREIGN KEY → users(id), NOT NULL | Project creator |
| `is_active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Project status |
| `issue_sequence` | INTEGER | NOT NULL, DEFAULT 0 | Counter for issue keys |
| `workflow_id` | UUID | NULL | Custom workflow (Phase 3) |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Creation time |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Last modification time |

**Indexes:**

- `idx_projects_key` on `project_key`
- `idx_projects_created_by` on `created_by`

---

### 2.3 project_members

Join table for project team membership.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| `project_id` | UUID | FOREIGN KEY → projects(id) ON DELETE CASCADE | Project reference |
| `user_id` | UUID | FOREIGN KEY → users(id) ON DELETE CASCADE | User reference |
| `role_in_project` | VARCHAR(30) | NOT NULL, DEFAULT 'CONTRIBUTOR' | Role within project |
| `joined_at` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Membership start |

**Constraints:**

- UNIQUE(`project_id`, `user_id`) - User can only be a member once

**Indexes:**

- `idx_pm_project` on `project_id`
- `idx_pm_user` on `user_id`

---

### 2.4 issues

Core issue tracking table.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| `project_id` | UUID | FOREIGN KEY → projects(id) ON DELETE CASCADE | Parent project |
| `issue_key` | VARCHAR(20) | UNIQUE, NOT NULL | Issue key (e.g., PROJ-1) |
| `title` | VARCHAR(200) | NOT NULL | Issue title |
| `description` | TEXT | NULL | Detailed description |
| `status` | VARCHAR(30) | NOT NULL, DEFAULT 'OPEN' | Current status |
| `priority` | VARCHAR(20) | NULL, DEFAULT 'MEDIUM' | Priority level (Phase 2) |
| `issue_type` | VARCHAR(30) | NULL, DEFAULT 'TASK' | Issue type (Phase 2) |
| `reporter_id` | UUID | FOREIGN KEY → users(id), NOT NULL | Issue creator |
| `assignee_id` | UUID | FOREIGN KEY → users(id), NULL | Assigned user |
| `parent_issue_id` | UUID | FOREIGN KEY → issues(id), NULL | Parent issue (Phase 2) |
| `due_date` | DATE | NULL | Target completion date |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Creation time |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT NOW() | Last modification time |

**Indexes:**

- `idx_issues_project` on `project_id`
- `idx_issues_status` on `status`
- `idx_issues_assignee` on `assignee_id`
- `idx_issues_reporter` on `reporter_id`
- `idx_issues_key` on `issue_key`

---

## 3. Enums and Valid Values

### 3.1 User Roles (Phase 1)

| Value | Description |
|-------|-------------|
| `ADMIN` | Full system access |
| `MEMBER` | Standard user access |

### 3.2 Issue Status (Phase 1)

| Value | Description |
|-------|-------------|
| `OPEN` | New issue, not started |
| `IN_PROGRESS` | Currently being worked on |
| `DONE` | Completed |

### 3.3 Priority Levels (Phase 2 - Placeholder)

| Value | Description |
|-------|-------------|
| `LOW` | Low priority |
| `MEDIUM` | Normal priority |
| `HIGH` | High priority |
| `CRITICAL` | Urgent priority |

### 3.4 Issue Types (Phase 2 - Placeholder)

| Value | Description |
|-------|-------------|
| `TASK` | General task |
| `BUG` | Bug/defect |
| `STORY` | User story |
| `EPIC` | Large feature |

---

## 4. Future Phase Tables (Placeholders)

### 4.1 issue_comments (Phase 2)

```sql
CREATE TABLE issue_comments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    issue_id UUID NOT NULL REFERENCES issues(id) ON DELETE CASCADE,
    author_id UUID NOT NULL REFERENCES users(id),
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
```

### 4.2 workflows (Phase 3)

```sql
CREATE TABLE workflows (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE workflow_transitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_id UUID NOT NULL REFERENCES workflows(id) ON DELETE CASCADE,
    from_status VARCHAR(30) NOT NULL,
    to_status VARCHAR(30) NOT NULL,
    required_role VARCHAR(30),
    UNIQUE(workflow_id, from_status, to_status)
);
```

### 4.3 audit_logs (Phase 4)

```sql
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type VARCHAR(50) NOT NULL,
    entity_id UUID NOT NULL,
    action VARCHAR(20) NOT NULL,
    actor_id UUID REFERENCES users(id),
    old_values JSONB,
    new_values JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
```

---

## 5. SQL Migration Script (Phase 1)

```sql
-- ============================================
-- Phase 1: Core Database Schema
-- Project & Issue Tracking System
-- ============================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================
-- Table: users
-- ============================================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    avatar_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'MEMBER'))
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);

-- ============================================
-- Table: projects
-- ============================================
CREATE TABLE projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_key VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_by UUID NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    issue_sequence INTEGER NOT NULL DEFAULT 0,
    workflow_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    CONSTRAINT uq_projects_key UNIQUE (project_key),
    CONSTRAINT fk_projects_created_by FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE INDEX idx_projects_key ON projects(project_key);
CREATE INDEX idx_projects_created_by ON projects(created_by);

-- ============================================
-- Table: project_members
-- ============================================
CREATE TABLE project_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role_in_project VARCHAR(30) NOT NULL DEFAULT 'CONTRIBUTOR',
    joined_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    CONSTRAINT fk_pm_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_pm_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_pm_project_user UNIQUE (project_id, user_id)
);

CREATE INDEX idx_pm_project ON project_members(project_id);
CREATE INDEX idx_pm_user ON project_members(user_id);

-- ============================================
-- Table: issues
-- ============================================
CREATE TABLE issues (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID NOT NULL,
    issue_key VARCHAR(20) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    issue_type VARCHAR(30) DEFAULT 'TASK',
    reporter_id UUID NOT NULL,
    assignee_id UUID,
    parent_issue_id UUID,
    due_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    CONSTRAINT uq_issues_key UNIQUE (issue_key),
    CONSTRAINT fk_issues_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_issues_reporter FOREIGN KEY (reporter_id) REFERENCES users(id),
    CONSTRAINT fk_issues_assignee FOREIGN KEY (assignee_id) REFERENCES users(id),
    CONSTRAINT fk_issues_parent FOREIGN KEY (parent_issue_id) REFERENCES issues(id),
    CONSTRAINT chk_issues_status CHECK (status IN ('OPEN', 'IN_PROGRESS', 'DONE'))
);

CREATE INDEX idx_issues_project ON issues(project_id);
CREATE INDEX idx_issues_status ON issues(status);
CREATE INDEX idx_issues_assignee ON issues(assignee_id);
CREATE INDEX idx_issues_reporter ON issues(reporter_id);
CREATE INDEX idx_issues_key ON issues(issue_key);

-- ============================================
-- Function: Auto-update updated_at timestamp
-- ============================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply trigger to all tables
CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_projects_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_issues_updated_at
    BEFORE UPDATE ON issues
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- Seed: Create default admin user (optional)
-- Password: admin123 (change immediately!)
-- ============================================
-- INSERT INTO users (username, email, password_hash, full_name, role)
-- VALUES ('admin', 'admin@example.com', '$2a$10$...', 'System Admin', 'ADMIN');
```

---

## 6. Design Decisions

| Decision | Rationale |
|----------|-----------|
| **UUID for primary keys** | Globally unique, safe for distributed systems, no sequential guessing |
| **Separate project_members table** | Many-to-many relationship, allows role per project |
| **issue_sequence in projects** | Generates sequential issue keys (PROJ-1, PROJ-2) per project |
| **Nullable priority/type columns** | Phase 2 features included as placeholders |
| **workflow_id placeholder** | Ready for Phase 3 custom workflows |
| **is_active flags** | Soft delete support instead of hard deletes |
| **updated_at triggers** | Automatic timestamp updates on modifications |
| **CHECK constraints** | Enforce valid enum values at database level |

---

## 7. Phase Compatibility Matrix

| Table/Column | P1 | P2 | P3 | P4 |
|--------------|----|----|----|----|
| users | ✅ | ✅ | ✅ | ✅ |
| projects | ✅ | ✅ | ✅ | ✅ |
| project_members | ✅ | ✅ | ✅ | ✅ |
| issues | ✅ | ✅ | ✅ | ✅ |
| issues.priority | ⏸️ | ✅ | ✅ | ✅ |
| issues.issue_type | ⏸️ | ✅ | ✅ | ✅ |
| issue_comments | ❌ | ✅ | ✅ | ✅ |
| workflows | ❌ | ❌ | ✅ | ✅ |
| audit_logs | ❌ | ❌ | ❌ | ✅ |

**Legend:** ✅ Active | ⏸️ Placeholder | ❌ Not yet created

---

*Document prepared for Phase 1 implementation with forward compatibility for Phases 2-4.*
