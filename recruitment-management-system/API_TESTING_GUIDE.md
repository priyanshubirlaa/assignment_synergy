# API Testing Guide

This guide provides examples for testing all the APIs in the Recruitment Management System.

## Prerequisites

1. Start the application: `mvn spring-boot:run`
2. Ensure MySQL database is running and configured
3. Use a tool like Postman, curl, or any REST client

## Base URL
```
http://localhost:8080/api
```

## Test Flow

### 1. Create Admin User

**POST** `/signup`
```json
{
    "name": "Admin User",
    "email": "admin@company.com",
    "password": "admin123",
    "userType": "ADMIN",
    "profileHeadline": "HR Manager",
    "address": "123 Admin St, City, Country"
}
```

### 2. Create Applicant User

**POST** `/signup`
```json
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "userType": "APPLICANT",
    "profileHeadline": "Software Developer",
    "address": "456 Developer Ave, City, Country"
}
```

### 3. Login as Admin

**POST** `/login`
```json
{
    "email": "admin@company.com",
    "password": "admin123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "message": "Login successful",
    "userId": 1,
    "userType": "ADMIN"
}
```

### 4. Create Job Posting (Admin)

**POST** `/admin/job`
**Headers:** `Authorization: Bearer <admin_token>`

```json
{
    "title": "Senior Software Developer",
    "description": "We are looking for an experienced software developer with expertise in Java, Spring Boot, and microservices architecture.",
    "companyName": "Tech Solutions Inc"
}
```

### 5. Login as Applicant

**POST** `/login`
```json
{
    "email": "john@example.com",
    "password": "password123"
}
```

### 6. Upload Resume (Applicant)

**POST** `/uploadResume`
**Headers:** `Authorization: Bearer <applicant_token>`
**Content-Type:** `multipart/form-data`
**Body:** Upload a PDF or DOCX file in the `file` field

### 7. View Available Jobs (Applicant)

**GET** `/jobs`
**Headers:** `Authorization: Bearer <applicant_token>`

### 8. Apply for Job (Applicant)

**GET** `/jobs/apply?job_id=1`
**Headers:** `Authorization: Bearer <applicant_token>`

### 9. View All Applicants (Admin)

**GET** `/admin/applicants`
**Headers:** `Authorization: Bearer <admin_token>`

### 10. View Job with Applicants (Admin)

**GET** `/admin/job/1`
**Headers:** `Authorization: Bearer <admin_token>`

### 11. View Applicant Profile (Admin)

**GET** `/admin/applicant/2`
**Headers:** `Authorization: Bearer <admin_token>`

## cURL Examples

### Signup
```bash
curl -X POST http://localhost:8080/api/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "userType": "APPLICANT",
    "profileHeadline": "Software Developer",
    "address": "123 Main St, City, Country"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Upload Resume
```bash
curl -X POST http://localhost:8080/api/uploadResume \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/your/resume.pdf"
```

### Create Job (Admin)
```bash
curl -X POST http://localhost:8080/api/admin/job \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Software Developer",
    "description": "Looking for a talented developer",
    "companyName": "Tech Corp"
  }'
```

### Apply for Job
```bash
curl -X GET "http://localhost:8080/api/jobs/apply?job_id=1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Expected Responses

### Successful Login Response
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "message": "Login successful",
    "userId": 1,
    "userType": "APPLICANT"
}
```

### Error Response
```json
{
    "error": "User not found"
}
```

### Job List Response
```json
{
    "jobs": [
        {
            "id": 1,
            "title": "Senior Software Developer",
            "description": "We are looking for...",
            "postedOn": "2024-01-15T10:30:00",
            "totalApplications": 2,
            "companyName": "Tech Solutions Inc",
            "postedBy": {
                "id": 1,
                "name": "Admin User",
                "email": "admin@company.com"
            }
        }
    ]
}
```

## Testing Tips

1. **Save JWT Tokens**: Copy the token from login response and use it in Authorization header
2. **Check User Types**: Ensure you're using the correct user type for each endpoint
3. **File Upload**: Test with both PDF and DOCX files
4. **Error Handling**: Try invalid credentials, missing tokens, wrong user types
5. **Database**: Check your database to see if data is being stored correctly

## Common Issues

1. **401 Unauthorized**: Check if JWT token is valid and included in Authorization header
2. **403 Forbidden**: Verify user has correct role for the endpoint
3. **400 Bad Request**: Check request body format and required fields
4. **File Upload Issues**: Ensure file is PDF or DOCX format and under 10MB
5. **Database Connection**: Verify MySQL is running and credentials are correct

## Sample Resume Files

For testing resume upload, you can use any PDF or DOCX file. The system will extract information using the third-party API and store it in the profile.

