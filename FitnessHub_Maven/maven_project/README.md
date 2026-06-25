# Fitness Hub

A JavaFX desktop application for gym management (members, trainers, bookings, memberships, fees), using an Oracle database for storage.

## Requirements

- Java JDK 21 or later
- Apache Maven 3.8+
- Oracle Database (XE or similar) running locally

## Database Setup

Update the connection details in `src/main/java/DatabaseConnection.java` to match your Oracle setup:

```java
private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
private static final String USER = "sys as sysdba";
private static final String PASSWORD = "1234";
```

> It's recommended to use a dedicated database user instead of SYSDBA, and to avoid committing real credentials to version control.

## Running the project

```bash
mvn clean javafx:run
```

Maven will automatically download JavaFX, the Oracle JDBC driver, and Gluon Charm Glisten — no manual library setup required.

## Building a jar

```bash
mvn clean package
```

## Project structure

```
src/main/java/        -> Java source files (controllers, DAO, models)
src/main/resources/   -> FXML views, CSS, and images
pom.xml                -> Maven build configuration
```
