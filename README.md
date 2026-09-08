# Student Performance Management System

A full-stack web application for managing students, subjects, and grades, with role-based access control for Admins and Teachers, built-in performance analytics, and a public results lookup for students.

## Features

- **Full CRUD** for Students, Subjects, and Grades, with bulk-insert endpoints for adding multiple records at once
- **Role-based access control** using Spring Security — Admins can manage students and subjects; Teachers can record grades; both require authentication
- **Password security** — user passwords are hashed with BCrypt before being stored
- **Performance analytics** — average marks per student, average marks per subject, top performer, and pass/fail summary, computed from live grade data
- **Public student lookup** — students can check their own results by roll number with no login required
- **Clean error handling** — validation errors and database constraint conflicts (e.g. trying to delete a student with existing grades) return readable JSON messages instead of raw stack traces
- **Frontend dashboard** — a lightweight HTML/CSS/JavaScript interface that authenticates against the backend and displays live data across Students, Subjects, Grades, and Analytics tabs

## Tech Stack

**Backend:** Java, Spring Boot, Spring Data JPA, Spring Security, Hibernate, MySQL
**Frontend:** HTML5, CSS3, JavaScript (vanilla, no framework)
**Tools:** Maven, Eclipse / Spring Tools, Postman (for API testing)

## Project Structure

```
StudentPerformanceSystem/
├── src/main/java/com/spms/
│   ├── model/         # Entity classes: User, Student, Subject, Grade, Role
│   ├── repository/    # Spring Data JPA repositories
│   ├── controller/    # REST controllers (Student, Subject, Grade, Auth, Analytics, Public)
│   ├── config/        # Security configuration, custom UserDetailsService
│   └── exception/      # Global exception handling
├── src/main/resources/
│   └── application.properties
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
└── pom.xml
```

## Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL Server running locally

### Backend Setup
1. Clone the repository:
   ```
   git clone https://github.com/Mounikasadu/student-performance-management-system.git
   ```
2. Create the database:
   ```sql
   CREATE DATABASE student_performance_db;
   ```
3. Set your database password as an environment variable named `DB_PASSWORD`, or edit the fallback value directly in `src/main/resources/application.properties`.
4. Run the application (via Eclipse/Spring Tools, or `mvn spring-boot:run`). The backend starts on `http://localhost:8080`.

### Frontend Setup
No build step required — the frontend is plain HTML/CSS/JS.
1. Open `frontend/index.html` directly in a browser.
2. Make sure the backend is running first, since the frontend calls it directly at `http://localhost:8080`.

## Default Roles

The system supports two roles:
- **ADMIN** — full access: manage students, subjects, and view/delete records
- **TEACHER** — can view students and record grades, but cannot delete students or manage subjects

Register a user via `POST /api/auth/register`:
```json
{
  "name": "Admin User",
  "email": "admin@spms.com",
  "password": "your-password",
  "role": "ADMIN"
}
```

## API Overview

| Endpoint | Method | Access | Description |
|---|---|---|---|
| `/api/auth/register` | POST | Public | Register a new user |
| `/api/students` | GET/POST/PUT/DELETE | Authenticated (write restricted to Admin) | Manage students |
| `/api/students/bulk` | POST | Admin | Bulk-create students |
| `/api/subjects` | GET/POST | Authenticated | Manage subjects |
| `/api/grades` | GET/POST | Admin, Teacher (POST) | Manage grades |
| `/api/analytics/average-per-student` | GET | Authenticated | Average marks per student |
| `/api/analytics/average-per-subject` | GET | Authenticated | Average marks per subject |
| `/api/analytics/top-performer` | GET | Authenticated | Highest scoring record |
| `/api/analytics/pass-fail-summary` | GET | Authenticated | Pass/fail counts |
| `/api/public/results/{rollNumber}` | GET | Public | Student self-lookup by roll number |

## Notes on Design Decisions

- **Basic Auth over JWT:** Authentication uses Spring Security's HTTP Basic Auth rather than JWT, to keep the scope focused and avoid unnecessary complexity for a project of this size. The role-based authorization logic is fully implemented either way.
- **Students are data, not accounts:** Students are managed entities rather than authenticated users, since the system is designed around Admin/Teacher usage. The public roll-number lookup endpoint exists specifically to give students self-service access without requiring full accounts.

## Future Improvements

- JWT-based authentication for stateless, scalable sessions
- Full student login with personalized dashboards
- Pagination and filtering on list endpoints
- Deployment to a live hosting environment

## Author

Mounika Sadu
