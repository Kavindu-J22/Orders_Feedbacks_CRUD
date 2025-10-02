# Food Ordering System with Ticket Management

A comprehensive Java Spring Boot web application for managing food orders and customer support tickets.

## Features

### 🍕 Order Management
- **CRUD Operations**: Create, Read, Update, Delete orders
- **Order Tracking**: Track order status from pending to delivered
- **Customer Information**: Store customer details, delivery address, and contact information
- **Food Items**: Manage food items with quantities and pricing in LKR currency
- **Search & Filter**: Advanced search functionality for orders

### 🎫 Ticket Management System
- **Create Tickets**: Submit support tickets linked to orders
- **Ticket Categories**: Food Quality Issue, Delivery Problem, Order Incorrect, Payment Issue, Customer Service, Technical Problem, Refund Request, General Inquiry
- **Priority Levels**: URGENT, HIGH, MEDIUM, LOW
- **Status Tracking**: OPEN, IN_PROGRESS, RESOLVED, CLOSED
- **Business Rules**: 
  - Tickets in progress or resolved cannot be edited (only replies can be added)
  - Status updates are allowed at any time
- **Reply System**: Add replies to tickets for communication

### 📊 Dashboard & Statistics
- **Overview Dashboard**: Professional statistics display
- **Ticket Statistics**: Total, open, in-progress, resolved tickets
- **Priority Distribution**: Breakdown by priority levels
- **Order Statistics**: Order status distribution
- **Category Analysis**: Tickets per category
- **Top Customers**: Top 5 customers by ticket/order count
- **Search & Filter**: Comprehensive filtering options

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0
- **Database**: MySQL (with H2 for testing)
- **Frontend**: Thymeleaf, Bootstrap 5.3.0, jQuery
- **Build Tool**: Maven
- **ORM**: JPA/Hibernate

## Project Structure

```
src/
├── main/
│   ├── java/com/foodorder/
│   │   ├── OrderFeedbacksApplication.java
│   │   ├── controller/
│   │   │   ├── HomeController.java
│   │   │   ├── OrderController.java
│   │   │   └── TicketController.java
│   │   ├── entity/
│   │   │   ├── Order.java
│   │   │   ├── Ticket.java
│   │   │   └── TicketReply.java
│   │   ├── repository/
│   │   │   ├── OrderRepository.java
│   │   │   ├── TicketRepository.java
│   │   │   └── TicketReplyRepository.java
│   │   └── service/
│   │       ├── OrderService.java
│   │       ├── TicketService.java
│   │       ├── StatisticsService.java
│   │       └── DataInitializationService.java
│   └── resources/
│       ├── application.properties
│       └── templates/
│           ├── dashboard.html
│           ├── layout.html
│           ├── orders/
│           │   ├── list.html
│           │   ├── form.html
│           │   └── view.html
│           └── tickets/
│               ├── list.html
│               ├── form.html
│               └── view.html
```

## Database Configuration

### For MySQL (Production)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/food_ordering_system?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=Shashini1223@
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### For H2 (Testing)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (for production) or H2 (for testing)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Order_Feedbacks
   ```

2. **Configure Database**
   - For MySQL: Update `application.properties` with your MySQL credentials
   - For H2: Use the current configuration (default)

3. **Build the application**
   ```bash
   mvn clean package
   ```

4. **Run the application**
   ```bash
   java -jar target/order-feedbacks-0.0.1-SNAPSHOT.jar
   ```
   
   Or using Maven:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Main Application: http://localhost:8080
   - H2 Console (if using H2): http://localhost:8080/h2-console

## Dummy Data

The application automatically populates the database with dummy data on first startup:
- **10 Orders** with Sri Lankan customer names and food items
- **8 Support Tickets** with various categories and priorities
- **Multiple Ticket Replies** for demonstration

## API Endpoints

### Orders
- `GET /orders` - List all orders with search/filter
- `GET /orders/new` - Create new order form
- `POST /orders` - Create new order
- `GET /orders/{id}` - View order details
- `GET /orders/{id}/edit` - Edit order form
- `POST /orders/{id}` - Update order
- `POST /orders/{id}/delete` - Delete order
- `POST /orders/{id}/status` - Update order status

### Tickets
- `GET /tickets` - List all tickets with search/filter
- `GET /tickets/new` - Create new ticket form
- `POST /tickets` - Create new ticket
- `GET /tickets/{id}` - View ticket details
- `GET /tickets/{id}/edit` - Edit ticket form
- `POST /tickets/{id}` - Update ticket
- `POST /tickets/{id}/delete` - Delete ticket
- `POST /tickets/{id}/status` - Update ticket status
- `POST /tickets/{id}/reply` - Add reply to ticket

### Dashboard
- `GET /` - Redirect to dashboard
- `GET /dashboard` - Main dashboard with statistics

## Features Implemented

✅ **Complete CRUD Operations** for Orders and Tickets  
✅ **Advanced Search & Filter** functionality  
✅ **Professional Dashboard** with statistics  
✅ **Ticket Reply System** with communication tracking  
✅ **Business Rules** enforcement (edit restrictions)  
✅ **Responsive Design** with Bootstrap  
✅ **Dummy Data Population** with Sri Lankan names  
✅ **Status Management** for orders and tickets  
✅ **Category-based Organization**  
✅ **Priority-based Ticket Management**  

## Usage Instructions

1. **Dashboard**: View overall statistics and quick actions
2. **Orders**: Manage food orders, update status, create support tickets
3. **Tickets**: Handle customer support requests, add replies, update status
4. **Search**: Use filters to find specific orders or tickets
5. **Create Ticket**: Link tickets to specific orders for better tracking

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please create an issue in the repository.
