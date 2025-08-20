
# Expense Tracker 



## How to Run Locally

Clone the project




Go to the project directory

```bash
  cd expense-tracker-api
```

Create a database

```bash
  CREATE DATABASE database_name
```

Configure database properties in "application.properties" file as per your need

```bash
  spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
  spring.datasource.username=root
  spring.datasource.password=
```

Build and Run the app

```bash
  mvn spring-boot:run
```

The app will start running at http://localhost:8080


## Swagger - API Documentation
http://localhost:8080/swagger-ui.html
