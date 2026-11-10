 # Task Manager - Spring Boot

A RESTful API for managing tasks, developed with Java and Spring Boot.

This project provides basic task management functionality, including creating, retrieving, updating, completing, and deleting tasks. It also implements request validation, global exception handling, and automated tests.

## Technologies

- Java 24
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- H2 Database
- Jakarta Validation
- Maven
- JUnit 5
- Mockito
- MockMvc

## Features

- Create new tasks.
- Retrieve all tasks.
- Retrieve a task by its ID.
- Update existing tasks.
- Mark tasks as completed.
- Delete tasks.
- Validate incoming requests.
- Handle exceptions globally.
- Unit and integration tests.

## Project Structure

```text
src/
├── main/
│   ├── java/com/taskManager/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
└── test/
    └── java/com/taskManager/
        ├── controller/
        └── service/
```

## Getting Started

### Prerequisites

- Java 24 or higher
- Maven

### Installation

1. Clone the repository:

```bash
git clone https://github.com/MxiRamos/Task-Manager-SpringBoot.git
```

2. Navigate to the project directory:

```bash
cd Task-Manager-SpringBoot
```

3. Run the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/tasks` | Retrieve all tasks |
| GET | `/api/tasks/{id}` | Retrieve a task by ID |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Update a task |
| PUT | `/api/tasks/{id}/complete` | Mark a task as completed |
| DELETE | `/api/tasks/{id}` | Delete a task |

### Example: Create a Task

**Request**

```http
POST /api/tasks
Content-Type: application/json
```

```json
{
    "title": "Learn Spring Boot",
    "description": "Practice REST APIs with Spring Boot"
}
```

**Response**

```json
{
    "id": 1,
    "title": "Learn Spring Boot",
    "description": "Practice REST APIs with Spring Boot",
    "completed": false
}
```

## Validation and Exception Handling

The API uses Jakarta Validation to validate incoming requests.

- Task titles are required and cannot exceed 100 characters.
- Task descriptions are required and cannot exceed 1000 characters.
- A global exception handler provides structured error responses.
- Requests for non-existent tasks return HTTP 404.

## Running Tests

Run the automated tests with:

```bash
./mvnw test
```

The project uses JUnit 5, Mockito, Spring Boot Test, and MockMvc.

## Author

Developed by [MxiRamos](https://github.com/MxiRamos).

# Task-Manager-SpringBoot
