# EMI Management System

A complete Spring Core + Hibernate project for managing EMI (Equated Monthly Installment) payments with Oracle Database integration.

## 📋 Project Overview

This project implements a multi-layered EMI management system with the following features:

- **Loan Account Validation**: Validate loan account numbers
- **EMI Calculation**: Calculate pending EMI amounts with penalty charges
- **Payment Processing**: Process payments with receipt generation
- **Payment Allocation**: Allocate payments with priority (Penalty → EMI)
- **Transaction Management**: Full Spring transaction support
- **REST API**: Complete REST endpoints for all operations

## 🛠️ Tech Stack

- **Java 17**
- **Spring Core 6**
- **Hibernate ORM 6**
- **Jakarta Persistence API 3.1**
- **Oracle Database**
- **Maven**
- **JUnit 5 + Mockito** (for testing)

## 🏗️ Architecture

The project follows a clean multi-layered architecture:

```
├── Entity Layer (Jakarta Persistence)
├── DAO Layer (Hibernate DAO classes)
├── Service Layer (Business logic + transactions)
└── Controller Layer (REST endpoints)
```

## 🗄️ Database Schema

### 1. LMS_RECEIVABLEPAYBLE_DTL_17557
Stores EMI receivable details:
- `RECEIVABLE_ID` (Primary Key)
- `LOAN_ACCOUNT_NO` (VARCHAR2(20))
- `PENDING_EMI_AMOUNT` (NUMBER(10,2))
- `PENALTY_CHARGES` (NUMBER(10,2))
- `TOTAL_AMOUNT` (NUMBER(10,2))
- `CREATED_DATE` (DATE)

### 2. LMS_RECEIPT_PAYMENT_DTL_17557
Stores user payment receipts:
- `RECEIPT_ID` (Primary Key)
- `LOAN_ACCOUNT_NO` (VARCHAR2(20))
- `PAYMENT_AMOUNT` (NUMBER(10,2))
- `PAYMENT_MODE` (VARCHAR2(20))
- `PAYMENT_DATE` (DATE)

### 3. LMS_ALLOCATION_DTL_17557_
Stores allocation details of payments:
- `ALLOCATION_ID` (Primary Key)
- `LOAN_ACCOUNT_NO` (VARCHAR2(20))
- `ALLOCATED_TO` (VARCHAR2(20)) → "Penalty" or "EMI"
- `ALLOCATED_AMOUNT` (NUMBER(10,2))
- `ALLOCATION_DATE` (DATE)

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Oracle Database (or Oracle XE)

### Database Setup

1. Execute the DDL scripts in `src/main/resources/ddl-scripts.sql` in your Oracle database
2. Update database connection details in `src/main/resources/application.properties`

### Configuration

Update the following in `application.properties`:

```properties
# Oracle Database Configuration
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Building the Project

```bash
mvn clean compile
```

### Running Tests

```bash
mvn test
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## 📡 API Endpoints

### 1. Validate Loan Account
```http
GET /api/emi/validate/{loanAccountNo}
```

**Response:**
```json
{
  "success": true,
  "valid": true,
  "loanAccountNo": "TEST123",
  "message": "Loan account exists"
}
```

### 2. Calculate EMI Details
```http
GET /api/emi/calculate/{loanAccountNo}
```

**Response:**
```json
{
  "success": true,
  "loanAccountNo": "TEST123",
  "pendingEmiAmount": 1000.00,
  "penaltyCharges": 50.00,
  "totalAmount": 1050.00,
  "message": "EMI details calculated successfully"
}
```

### 3. Process Payment
```http
POST /api/emi/payment
Content-Type: application/json

{
  "loanAccountNo": "TEST123",
  "paymentAmount": 500.00,
  "paymentMode": "Cash"
}
```

**Response:**
```json
{
  "success": true,
  "receiptId": 1,
  "loanAccountNo": "TEST123",
  "paymentAmount": 500.00,
  "paymentMode": "Cash",
  "paymentDate": "2024-01-15T10:30:00.000+00:00",
  "message": "Payment processed successfully"
}
```

### 4. Get Allocation Details
```http
GET /api/emi/allocations/{loanAccountNo}
```

### 5. Get Payment History
```http
GET /api/emi/history/{loanAccountNo}
```

## 🧪 Testing

The project includes comprehensive unit tests using JUnit 5 and Mockito:

- Service layer testing with mocked dependencies
- Test coverage for all business logic
- Proper assertion and verification

Run tests with:
```bash
mvn test
```

## 📁 Project Structure

```
emi-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/supernova/emims/
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── dao/             # DAO interfaces
│   │   │   ├── dao/impl/        # DAO implementations
│   │   │   ├── service/         # Service interfaces
│   │   │   ├── service/impl/    # Service implementations
│   │   │   ├── controller/      # REST controllers
│   │   │   └── config/          # Configuration classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── ddl-scripts.sql
│   └── test/
│       └── java/com/supernova/emims/service/
│           └── EmiManagementServiceTest.java
├── pom.xml
├── README.md
└── TODO.md
```

## 🔧 Key Features

### Business Logic
- **Penalty Calculation**: ₹10 per day if EMI is delayed
- **Payment Allocation**: Priority-based allocation (Penalty → EMI)
- **Transaction Management**: Full ACID compliance
- **Error Handling**: Comprehensive exception handling

### Technical Features
- **Clean Architecture**: Separation of concerns
- **Dependency Injection**: Spring IoC container
- **Connection Pooling**: HikariCP integration
- **SQL Logging**: Detailed query logging for debugging
- **Sonar Compliance**: Code quality standards

## 🔒 Security Considerations

- Input validation on all endpoints
- SQL injection prevention through parameterized queries
- Transaction rollback on errors
- Proper error message handling (no sensitive data exposure)

## 📊 Performance Optimizations

- Hibernate batch processing
- Connection pooling configuration
- Query optimization with proper indexing
- Lazy loading strategies

## 🐛 Error Handling

The application includes comprehensive error handling:

- **Validation Errors**: 400 Bad Request
- **Not Found Errors**: 404 Not Found
- **Server Errors**: 500 Internal Server Error
- **Transaction Rollback**: Automatic on exceptions

## 📝 Usage Examples

### Sample Workflow

1. **Validate Account**: Check if loan account exists
2. **Calculate EMI**: Get pending amount and penalty
3. **Process Payment**: Make payment and generate receipt
4. **Check Allocations**: View how payment was allocated
5. **View History**: Get complete payment history

### Example API Calls

```bash
# 1. Validate account
curl -X GET http://localhost:8080/api/emi/validate/TEST123

# 2. Calculate EMI
curl -X GET http://localhost:8080/api/emi/calculate/TEST123

# 3. Process payment
curl -X POST http://localhost:8080/api/emi/payment \
  -H "Content-Type: application/json" \
  -d '{
    "loanAccountNo": "TEST123",
    "paymentAmount": 500.00,
    "paymentMode": "Online"
  }'

# 4. Get allocations
curl -X GET http://localhost:8080/api/emi/allocations/TEST123

# 5. Get payment history
curl -X GET http://localhost:8080/api/emi/history/TEST123
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the API documentation

---

**Built with ❤️ using Spring Core 6 + Hibernate 6**
# emi-management-system
