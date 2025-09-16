# fuel-pass-backend

A robust Spring Boot REST API for managing fuel orders in aviation operations. Built with Java, Spring Security, JWT authentication, and PostgreSQL database integration.

🚀 Features

RESTful API: Complete CRUD operations for fuel orders and user management

JWT Authentication: Secure token-based authentication with HTTP-only cookies

Role-Based Access Control: Support for Aircraft Operators and Operations Managers

PostgreSQL Integration: Reliable data persistence with Hibernate ORM

Comprehensive Validation: Input validation and error handling

CORS Support: Cross-origin resource sharing for frontend integration

Security: Spring Security with JWT filters and authentication entry points

🛠️ Tech Stack

Framework: Spring Boot 3.x

Language: Java 17+

Database: PostgreSQL

ORM: Hibernate/JPA

Security: Spring Security + JWT

Build Tool: Maven

Documentation: Spring Boot Actuator

📋 Prerequisites

Before you begin, ensure you have the following installed:

Java 17 or higher

Maven 3.6+

PostgreSQL 12+

Git

🚀 Getting Started

1. Clone the Repository

   git clone https://github.com/ZaidSultan10/fuel-pass-backend.git
   cd fuel-pass-backend

2. Database Setup
   
Install PostgreSQL

Download and install PostgreSQL from postgresql.org

Create a database named fuel_pass_db

Note your database credentials (username, password, port)

Database Configuration

Update src/main/resources/application.yml:

3. Environment Configuration
   
Create src/main/resources/application.yml:

4. Build and Run
   
The API will be available at http://localhost:8080

📁 Project Structure

�� Authentication & Authorization

User Roles

AIRCRAFT_OPERATOR: Can create and view their own fuel orders

OPERATIONS_MANAGER: Can view all orders and update order statuses

JWT Token Structure

{
  "sub": "user@example.com",
  "userId": "uuid",
  "role": "AIRCRAFT_OPERATOR",
  "iat": 1234567890,
  "exp": 1234654290
}

📡 API Endpoints

Authentication

Method	Endpoint	Description	Access

POST	/auth/login	User login	Public

POST	/auth/logout	User logout	Authenticated

POST	/auth/refresh	Refresh token	Authenticated

Fuel Orders

Method	Endpoint	Description	Access

GET	/fuel-orders	Get all orders (paginated)	Operations Manager

GET	/fuel-orders/{id}	Get order by ID	Authenticated

POST	/fuel-orders	Create new order	Aircraft Operator

PATCH	/fuel-orders/{id}/status	Update order status	Operations Manager

GET	/fuel-orders/statistics	Get order statistics	Operations Manager

GET	/fuel-orders/user/{userId}	Get orders by user	Authenticated

GET	/fuel-orders/airport/{airportCode}	Get orders by airport	Operations Manager

Users

Method	Endpoint	Description	Access

GET	/users/me	Get current user	Authenticated

GET	/users/statistics	Get user statistics	Operations Manager

📊 Database Schema

Users Table

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Fuel Orders Table

CREATE TABLE fuel_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tail_number VARCHAR(10) NOT NULL,
    airport_icao_code VARCHAR(4) NOT NULL,
    requested_fuel_volume DECIMAL(10,2) NOT NULL,
    delivery_time_window_start TIMESTAMP NOT NULL,
    delivery_time_window_end TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Airport Table

CREATE TABLE airports (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    icao_code VARCHAR(4) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO airports (icao_code, name, country) VALUES
    ('KJFK', 'John F. Kennedy International Airport', 'United States'),
    ('EGLL', 'London Heathrow Airport', 'United Kingdom'),
    ('EDDF', 'Frankfurt Airport', 'Germany'),
    ('RJTT', 'Tokyo Haneda Airport', 'Japan'),
    ('YSSY', 'Sydney Kingsford Smith Airport', 'Australia');

🔧 Configuration

Environment Variables

Variable	Description	Default

DB_USERNAME	PostgreSQL username	postgres

DB_PASSWORD	PostgreSQL password	password

JWT_SECRET	JWT signing secret	your-super-secret-jwt-key-here

SERVER_PORT	Server port	8080

CORS Configuration

The backend is configured to accept requests from:

http://localhost:3000 (Next.js development)

http://localhost:3001 (Alternative Next.js port)

🧪 Testing

Manual Testing with cURL

Login

Create Fuel Order

🚀 Deployment

Docker Deployment

Create Dockerfile:

Build and run:

Production Configuration

For production deployment:

Database: Use managed PostgreSQL service (AWS RDS, Google Cloud SQL, etc.)

Security: Use strong JWT secrets and HTTPS

Monitoring: Enable Spring Boot Actuator endpoints

Logging: Configure proper logging levels

🔒 Security Features

JWT Authentication: Stateless authentication with HTTP-only cookies

Password Encryption: BCrypt password hashing

CORS Protection: Configured for specific origins

Input Validation: Comprehensive request validation

SQL Injection Protection: JPA/Hibernate parameterized queries

�� Monitoring & Health Checks

Spring Boot Actuator endpoints (if enabled):

/actuator/health - Application health status

/actuator/info - Application information

/actuator/metrics - Application metrics

🤝 Frontend Integration

This backend is designed to work with the Fuel Pass Frontend:

API Base URL: http://localhost:8080

Authentication: JWT tokens with HTTP-only cookies

CORS: Configured for frontend domains

Error Handling: Consistent error response format

�� Troubleshooting

Common Issues

Database Connection Error

Verify PostgreSQL is running

Check database credentials in application.yml

Ensure database exists

JWT Token Issues

Verify JWT secret is configured

Check token expiration settings

Ensure proper cookie handling

CORS Errors

Verify frontend URL in CORS configuration

Check if credentials are included in requests

📝 API Response Format

Success Response

Error Response

🤝 Contributing

Fork the repository

Create a feature branch (git checkout -b feature/amazing-feature)

Commit your changes (git commit -m 'Add some amazing feature')

Push to the branch (git push origin feature/amazing-feature)

Open a Pull Request

📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

🆘 Support

If you encounter any issues:

Check the Issues page

Create a new issue with detailed information

Contact the development team

Built with ❤️ using Spring Boot and modern Java technologies

Repository: https://github.com/ZaidSultan10/fuel-pass-backend
