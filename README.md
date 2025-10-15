# Recruitment Management System - Backend API

A comprehensive backend server for a Recruitment Management System built with Spring Boot, featuring user authentication, resume parsing, and job management.

## Features

- **User Management**: Signup and login with JWT authentication
- **Role-based Access**: Admin and Applicant user types with different permissions
- **Resume Upload & Parsing**: Upload PDF/DOCX resumes and extract information using third-party API
- **Job Management**: Create and manage job postings (Admin only)
- **Job Applications**: Apply for jobs and track applications
- **Profile Management**: Store and retrieve applicant profiles with parsed resume data

## Tech Stack

- **Backend**: Spring Boot 3.5.6
- **Database**: MySQL
- **Security**: Spring Security with JWT
- **File Handling**: Multipart file upload
- **External API**: Resume parsing via API Layer
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Database Setup

Create a MySQL database:
```sql
CREATE DATABASE recruitment_db;
```

### 2. Configuration

Update the `application.properties` file with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/recruitment_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### POST /api/signup
Create a new user account.

**Request Body:**
```json
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "userType": "APPLICANT",
    "profileHeadline": "Software Developer",
    "address": "123 Main St, City, Country"
}
```

#### POST /api/login
Authenticate user and get JWT token.

**Request Body:**
```json
{
    "email": "john@example.com",
    "password": "password123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "message": "Login successful",
    "userId": 1,
    "userType": "APPLICANT"
}
```

### Resume Management

#### POST /api/uploadResume
Upload and parse resume file (Applicant only).

**Headers:**
- `Authorization: Bearer <jwt_token>`
- `Content-Type: multipart/form-data`

**Request Body:**
- `file`: Resume file (PDF or DOCX only)

### Job Management

#### GET /api/jobs
Get all available jobs.

**Headers:**
- `Authorization: Bearer <jwt_token>`

#### GET /api/jobs/apply?job_id={job_id}
Apply for a specific job (Applicant only).

**Headers:**
- `Authorization: Bearer <jwt_token>`

### Admin Endpoints

#### POST /api/admin/job
Create a new job posting (Admin only).

**Headers:**
- `Authorization: Bearer <jwt_token>`

**Request Body:**
```json
{
    "title": "Senior Software Developer",
    "description": "We are looking for an experienced developer...",
    "companyName": "Tech Corp"
}
```

#### GET /api/admin/job/{job_id}
Get job details with applicant list (Admin only).

**Headers:**
- `Authorization: Bearer <jwt_token>`

#### GET /api/admin/applicants
Get all applicants in the system (Admin only).

**Headers:**
- `Authorization: Bearer <jwt_token>`

#### GET /api/admin/applicant/{applicant_id}
Get applicant profile with parsed resume data (Admin only).

**Headers:**
- `Authorization: Bearer <jwt_token>`

## Database Schema

### Users Table
- `id`: Primary key
- `name`: User's full name
- `email`: Unique email address
- `address`: User's address
- `user_type`: ADMIN or APPLICANT
- `password_hash`: Encrypted password
- `profile_headline`: User's profile headline

### Profiles Table
- `id`: Primary key
- `applicant_id`: Foreign key to Users table
- `resume_file_address`: Path to uploaded resume file
- `skills`: Extracted skills from resume
- `education`: Extracted education information
- `experience`: Extracted work experience
- `name`: Name extracted from resume
- `email`: Email extracted from resume
- `phone`: Phone number extracted from resume

### Jobs Table
- `id`: Primary key
- `title`: Job title
- `description`: Job description
- `posted_on`: Job posting date
- `total_applications`: Number of applications received
- `company_name`: Company name
- `posted_by`: Foreign key to Users table (Admin)

### Job Applications Table
- `id`: Primary key
- `job_id`: Foreign key to Jobs table
- `applicant_id`: Foreign key to Users table
- `applied_at`: Application date
- `status`: Application status

## Security Features

- JWT-based authentication
- Role-based access control
- Password encryption using BCrypt
- File type validation for resume uploads
- Secure file storage

## Error Handling

All endpoints return appropriate HTTP status codes and error messages:
- `400 Bad Request`: Invalid input or business logic errors
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server errors

## File Upload

- Maximum file size: 10MB
- Supported formats: PDF, DOCX
- Files are stored in `./uploads/resumes/` directory
- Resume parsing is handled by external API

## Testing the API

You can test the API using tools like Postman or curl. Here's an example flow:

1. **Signup**: Create a new user account
2. **Login**: Get JWT token
3. **Upload Resume**: Upload a resume file (for applicants)
4. **Create Job**: Create job posting (for admins)
5. **Apply for Job**: Apply for available jobs (for applicants)
6. **View Applications**: View job applications (for admins)

## Configuration Properties

Key configuration properties in `application.properties`:

- `jwt.secret`: Secret key for JWT token signing
- `jwt.expiration`: JWT token expiration time in milliseconds
- `resume.parser.api.url`: Resume parsing API endpoint
- `resume.parser.api.key`: API key for resume parsing service
- `file.upload-dir`: Directory for storing uploaded files

## Project Structure

```
src/main/java/com/synergy/synlabs/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── dto/             # Data transfer objects
├── model/           # Entity classes
├── repository/      # Data access layer
├── security/        # Security configuration
└── service/         # Business logic layer
```
