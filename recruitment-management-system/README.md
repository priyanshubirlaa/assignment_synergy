# Recruitment Management System

A comprehensive backend server for a Recruitment Management System built with Spring Boot, featuring user management, job posting, resume parsing, and application tracking.

## 🚀 Features

- **User Management**: Registration and authentication for Admin and Applicant users
- **JWT Authentication**: Secure token-based authentication
- **Job Management**: Admin users can create and manage job openings
- **Resume Upload**: Applicants can upload PDF/DOCX resumes
- **Resume Parsing**: Integration with third-party API for automatic resume data extraction
- **Application Tracking**: Complete job application workflow
- **Role-based Access Control**: Different permissions for Admin and Applicant users

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.x
- **Database**: MySQL
- **Security**: Spring Security with JWT
- **File Upload**: Multipart file handling
- **API Integration**: REST API for resume parsing
- **Build Tool**: Maven

## 📋 API Endpoints

### Authentication
- `POST /api/signup` - User registration
- `POST /api/login` - User authentication

### Job Management
- `POST /api/admin/job` - Create job (Admin only)
- `GET /api/jobs` - View all jobs
- `GET /api/jobs/apply` - Apply for job (Applicant only)

### Admin Operations
- `GET /api/admin/job/{jobId}` - View job with applicants
- `GET /api/admin/applicants` - View all applicants
- `GET /api/admin/applicant/{applicantId}` - View applicant profile

### Resume Management
- `POST /api/uploadResume` - Upload resume (Applicant only)

## 🗄️ Database Models

### User
- Name, Email, Address
- UserType (Admin/Applicant)
- PasswordHash, ProfileHeadline

### Profile
- Resume file address
- Extracted data: Skills, Education, Experience
- Contact information

### Job
- Title, Description, Company Name
- Posted date, Application count
- Posted by (Admin user)

### JobApplication
- Job and Applicant references
- Application status and timestamp

## 🔧 Setup Instructions

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Configuration
1. Update `application.properties` with your database credentials
2. Set up MySQL database: `recruitment_db`
3. Configure resume parsing API key

### Running the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔐 Security Features

- JWT token-based authentication
- Role-based access control (RBAC)
- Password hashing with BCrypt
- Secure file upload validation (PDF/DOCX only)

## 📁 Project Structure

```
recruitment-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/synergy/synlabs/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/            # Data transfer objects
│   │   │   ├── model/          # Entity models
│   │   │   ├── repository/     # Data repositories
│   │   │   ├── security/       # Security configuration
│   │   │   └── service/        # Business logic services
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## 🧪 Testing

Comprehensive API testing guide available in `API_TESTING_GUIDE.md`

### Quick Test Commands
```bash
# Register Admin
curl -X POST http://localhost:8080/api/signup \
  -H "Content-Type: application/json" \
  -d '{"name":"Admin User","email":"admin@test.com","password":"admin123","userType":"ADMIN","profileHeadline":"HR Manager","address":"123 Admin St"}'

# Register Applicant
curl -X POST http://localhost:8080/api/signup \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Applicant","email":"applicant@test.com","password":"app123","userType":"APPLICANT","profileHeadline":"Software Developer","address":"456 Dev St"}'
```

## 📝 License

This project is developed as part of an assignment for Synergy Labs.

## 👨‍💻 Author

**Priyanshu Birla**
- GitHub: [@priyanshubirlaa](https://github.com/priyanshubirlaa)

## 🔗 Repository

[GitHub Repository](https://github.com/priyanshubirlaa/assignment_synergy)

---

**Note**: This system is fully functional and ready for production use. All APIs have been tested and verified to work correctly.