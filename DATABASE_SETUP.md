# Database Setup Guide

This application supports both H2 (in-memory) and MySQL databases. Follow the instructions below to configure your preferred database.

## Current Configuration

The application is currently configured to use **H2 database** for easy testing and demonstration.

## H2 Database (Default - For Testing)

### Advantages:
- No installation required
- Automatic setup
- Perfect for testing and development
- Data is reset on each restart (good for testing)

### Configuration:
The current `application.properties` is already configured for H2:

```properties
# Database Configuration - Using H2 for testing
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

### Access H2 Console:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## MySQL Database (For Production)

### Prerequisites:
1. Install MySQL 8.0 or higher
2. Create a database named `food_ordering_system`
3. Ensure MySQL is running on localhost:3306

### Setup Steps:

1. **Create Database:**
   ```sql
   CREATE DATABASE food_ordering_system;
   ```

2. **Update application.properties:**
   Comment out the H2 configuration and uncomment the MySQL configuration:

   ```properties
   # Database Configuration - Using MySQL for production
   spring.datasource.url=jdbc:mysql://localhost:3306/food_ordering_system?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=Shashini1223@
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   # H2 Configuration (comment out for MySQL)
   # spring.datasource.url=jdbc:h2:mem:testdb
   # spring.datasource.driverClassName=org.h2.Driver
   # spring.datasource.username=sa
   # spring.datasource.password=password
   # spring.h2.console.enabled=true

   # JPA Configuration
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   spring.jpa.properties.hibernate.format_sql=true
   ```

3. **Update Database Password:**
   Change the password in the configuration to match your MySQL root password:
   ```properties
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```

4. **Rebuild and Run:**
   ```bash
   mvn clean package
   java -jar target/order-feedbacks-0.0.1-SNAPSHOT.jar
   ```

## Database Schema

The application will automatically create the following tables:

### Orders Table
- `id` (Primary Key)
- `customer_name`
- `customer_email`
- `customer_phone`
- `delivery_address`
- `food_items`
- `total_amount`
- `currency`
- `status`
- `order_date`
- `special_instructions`

### Tickets Table
- `id` (Primary Key)
- `title`
- `description`
- `priority`
- `category`
- `customer_name`
- `customer_email`
- `customer_phone`
- `status`
- `created_date`
- `updated_date`
- `resolved_date`
- `order_id` (Foreign Key)

### Ticket Replies Table
- `id` (Primary Key)
- `message`
- `author_name`
- `author_email`
- `created_date`
- `ticket_id` (Foreign Key)

## Dummy Data

The application includes a `DataInitializationService` that automatically populates the database with sample data:

- **10 Sample Orders** with Sri Lankan customer names
- **8 Sample Tickets** with various categories and priorities
- **Multiple Ticket Replies** for demonstration

This data is created only if the database is empty (on first startup).

## Troubleshooting

### MySQL Connection Issues:
1. Ensure MySQL is running
2. Check username and password
3. Verify database exists
4. Check firewall settings

### H2 Console Not Accessible:
1. Ensure `spring.h2.console.enabled=true`
2. Check if application is running
3. Use correct JDBC URL: `jdbc:h2:mem:testdb`

### Data Not Persisting (H2):
This is expected behavior with H2 in-memory database. Data is reset on each restart. For persistent data, use MySQL.

## Switching Between Databases

To switch from H2 to MySQL or vice versa:

1. Stop the application
2. Update `application.properties` 
3. Rebuild: `mvn clean package`
4. Restart: `java -jar target/order-feedbacks-0.0.1-SNAPSHOT.jar`

## Performance Considerations

- **H2**: Faster startup, good for development
- **MySQL**: Better for production, persistent data, better performance with large datasets

Choose H2 for development/testing and MySQL for production deployment.
