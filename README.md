# Task & Project Tracker

A full-featured **Full-Stack Web Application** for managing Projects and Tasks. It allows users to create projects, add tasks 
formatted in a **Kanban Board** (To Do, In Progress, Done), and assign them to other registered platform users.

---

## Tecnologies & Tools

**Backend:**
* **Java 17 / 21**
*  **Spring Boot 3** (Spring Data JPA, Spring Web, Validation)
*  **PostgreSQL** (Relational Database)
*  **Lombok** & **Maven**

  **Frontend:**
  * **Angular** (Standalone Components, RxJS, Reactive Router)
  *  **TypeScript**, **HTML5 / SCSS**

---

## Key Features

* **User Auth & Mangement: ** User Registration and Login with secure session storage in `lacalStorage`.
* **Project CRUD:** Create, view, and delete projects per logged-in user (Owner).
*  **Kanban Task Board:**
  * Create tasks with title, description, priority (`LOW`, `MEDIUM`, `HIGH`), and due date.
  * Ability to **assign** a task to any user in the database.
  * Real-time status updates (`TODO` -> `IN_PROGRESS` -> `DONE`).
* **Guards & Navigation:** Route protection (Auth Guards) and automatic browser history management (`replaceUrl`).

---

## System Requirements (Prerequisites)
Before you begin, ensure you have installed:
* [Java Development Kit (JDK 17+)](https://www.oracle.com/java/technologies/downloads/)
* [Node.js (v18+)](https://nodejs.org/) & `npm`
* [Angular CLI](https://angular.io/cli) (`npm install -g @angular/cli`)
* [PostgreSQL](https://www.postgresql.org/download/)

---

## Installation & Setup Instructions

### 1. Database Configuration (PostgreSQL)

Open **pgAdmin** or your PostgreSQL terminal and create a new database:

```sql
CREATE DATABASE task_tracker;
```

---

### 2. Running the Backend (Spring Boot)

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Open `src/main/resources/application.properties` and update your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/task_tracker
   spring.datasource.username=YOUR_POSTGRES_USERNAME
   spring.datasource.password=YOUR_POSTGRES_PASSWORD
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   *The API will be running at: `http://localhost:8080`*

---

### 3. Running the Frontend (Angular)

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Angular server:
   ```bash
   ng serve
   ```
4. Open your browser at:
   ```text
   http://localhost:4200
   ```

   ---

## API Endpoints Summary

###  Users (`/api/users`)
* `POST /api/users/register` - Register a new user
* `POST /api/users/login` - User login
* `GET /api/users` - Fetch all users (for task assignment)

###  Projects (`/api/projects`)
* `POST /api/projects` - Create project (`{ name, description, ownerId }`)
* `GET /api/projects/owner/{ownerId}` - Fetch projects for a specific user
* `GET /api/projects/{id}` - Fetch project along with its tasks
* `DELETE /api/projects/{id}` - Delete project

###  Tasks (`/api/tasks`)
* `POST /api/tasks` - Create task (`{ title, status, priority, projectId, assigneeId }`)
* `GET /api/tasks/project/{projectId}` - Fetch tasks of a project
* `PUT /api/tasks/{taskId}` - Update task / change status
* `DELETE /api/tasks/{taskId}` - Delete task
