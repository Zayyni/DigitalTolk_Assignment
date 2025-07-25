# Translation Management Service

A comprehensive Spring Boot application for managing translations across multiple locales with advanced features like tagging, search, and JSON export capabilities.

## Features

- **Multi-locale Translation Management**: Support for multiple languages (en, fr, es, de, etc.)
- **Tag-based Organization**: Categorize translations with context tags (mobile, desktop, web)
- **Advanced Search**: Search by key, locale, content, or tags
- **JSON Export**: Optimized endpoint for frontend applications
- **Token-based Authentication**: JWT authentication with role-based access control
- **Performance Optimized**: Caching, bulk operations, and optimized queries
- **Comprehensive Testing**: 95%+ test coverage with unit and integration tests
- **Docker Support**: Containerized deployment with Docker Compose
- **API Documentation**: Swagger/OpenAPI documentation

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: MySQL 8.0 (H2 for testing)
- **Security**: Spring Security with JWT
- **Documentation**: SpringDoc OpenAPI 3
- **Testing**: JUnit 5, Mockito
- **Caching**: Spring Cache
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.0+ (or Docker)

### Running with Docker (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd translation-service
   ```

2. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html

### Running Locally

1. **Setup MySQL Database**
   ```sql
   CREATE DATABASE translation_db;
   ```

2. **Update application.yml**
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/translation_db
       username: your_username
       password: your_password
   ```

3. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Translation Management
- `POST /api/translations` - Create translation
- `PUT /api/translations/{id}` - Update translation
- `GET /api/translations/{id}` - Get translation by ID
- `DELETE /api/translations/{id}` - Delete translation (Admin only)
- `GET /api/translations/search` - Search translations
- `GET /api/translations/export/{locale}` - Export translations for locale
- `GET /api/translations/locales` - Get all available locales

### Admin
- `POST /api/admin/seed/{count}` - Seed database with test data

## Sample API Usage

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "fullName": "John Doe"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

### 3. Create Translation
```bash
curl -X POST http://localhost:8080/api/translations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "translationKey": "welcome.message",
    "locale": "en",
    "content": "Welcome to our application!",
    "tags": ["mobile", "ui"]
  }'
```

### 4. Search Translations
```bash
curl -X GET "http://localhost:8080/api/translations/search?locale=en&tags=mobile" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Export Translations (Public Endpoint)
```bash
curl -X GET http://localhost:8080/api/translations/export/en
```

## Performance Features

### Caching
- Translation queries are cached for improved response times
- Cache invalidation on updates/deletes
- Configurable cache managers

### Database Optimization
- Indexed columns for fast lookups
- Batch operations for bulk inserts
- Optimized JPA queries
- Connection pooling

### Bulk Operations
Seed database with 100K+ records for performance testing:
```bash
curl -X POST http://localhost:8080/api/admin/seed/10000 \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

## Testing

### Run All Tests
```bash
mvn test
```

### Test Coverage Report
```bash
mvn jacoco:report
# View report at target/site/jacoco/index.html
```

### Performance Testing
The application includes a data seeder that can create 100K+ records across multiple locales for performance testing.

## Security

### JWT Authentication
- Token-based authentication
- Role-based access control (ADMIN, USER)
- Configurable token expiration
- Secure password encoding with BCrypt

### Default Users
- Admin: `admin@digitaltolk.com` / `admin123`
- User: `user@digitaltolk.com` / `user123`

## Database Schema

### Core Tables
- `translations` - Main translation data
- `tags` - Context tags
- `translation_tags` - Many-to-many relationship
- `users` - User authentication

### Indexes
- Composite index on (translation_key, locale)
- Individual indexes on frequently queried columns
- Foreign key indexes for optimal joins

## Configuration

### Key Properties
```yaml
app:
  jwt:
    secret: your-secret-key
    expiration: 86400000 # 24 hours

spring:
  jpa:
    hibernate:
      batch_size: 1000
    properties:
      hibernate:
        order_inserts: true
        order_updates: true
```

## Design Decisions

### Architecture
- **Three-layer architecture**: Controller → Service → Repository
- **SOLID principles**: Dependency injection, single responsibility
- **Domain-driven design**: Clear entity relationships

### Performance Optimizations
- **Caching strategy**: Method-level caching with cache eviction
- **Batch processing**: Bulk operations for large datasets
- **Query optimization**: Indexed columns and efficient JPA queries
- **Connection pooling**: Optimized database connections

### Security Considerations
- **JWT tokens**: Stateless authentication
- **Role-based access**: Different permissions for admin/user
- **Input validation**: Bean validation on all DTOs
- **SQL injection prevention**: JPA parameterized queries

## Monitoring and Health

### Health Endpoints
- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

## CDN Support

The application is designed to support CDN integration for the JSON export endpoint:
- Cacheable responses with appropriate headers
- ETags for efficient caching
- Gzip compression support

## Development

### Code Style
- Follows standard Java conventions
- Comprehensive JavaDoc documentation
- Clean code principles

### Git Workflow
```bash
git clone <repository-url>
git checkout -b feature/your-feature
# Make changes
git commit -m "Add your feature"
git push origin feature/your-feature
```

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Verify MySQL is running
   - Check connection string and credentials
   - Ensure database exists

2. **Authentication Failures**
   - Verify JWT token is included in Authorization header
   - Check token expiration
   - Ensure user exists and is active

3. **Performance Issues**
   - Check cache configuration
   - Monitor database query performance
   - Consider increasing connection pool size

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review application logs
3. Consult API documentation at `/swagger-ui.html`

## License

This project is created for DigitalTolk coding assessment.
