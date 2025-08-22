
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
HLD
<img width="1432" height="898" alt="HLD" src="https://github.com/user-attachments/assets/d4cf88f2-a756-44e8-956a-c6d16405f71d" />




User Registration
curl --location 'http://localhost:8080/api/users/register' \
--header 'accept: */*' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=56C682C168DB695C41EFDA75198E62F5' \
--data-raw '{ 
  "email": "test@abc.com",
  "firstName": "test5",
  "lastName": "hack",
  "mobile": 88888888,
  "password": "hack",
  "preferredContactMethod": "Mobile"
}'




{
    "Message": "Token generated successfully",
    "UserId": "7",
    "Token": "<<sample token>>"
}







User Login
curl --location 'http://localhost:8080/api/users/login' \
--header 'accept: */*' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=56C682C168DB695C41EFDA75198E62F5' \
--data-raw '{
  "email": "abc@test.com",
  "firstName": "test",
  "lastName": "hack",
  "password": "hack"
}'




{
    "Message": "Token generated successfully",
    "userId": "1",
    "token": "<<Sample token>>"
}







Adding Expense

Headers (for Authenticated Users):

Authorization: Bearer <JWT_TOKEN>

curl --location 'http://localhost:8080/api/expenses/transactions' \
--header 'accept: */*' \
--header 'Content-Type: application/json' \
--header 'Authorization: ••••••' \
--header 'Cookie: JSESSIONID=56C682C168DB695C41EFDA75198E62F5' \
--data '{
    "inputType": "USER",
    "userInput": {
        "amount": 100,
        "categoryId": 1,
        "currency": "INR",
        "customerName": "Sameer",
        "description": "zomato",
        "invoiceDate": "2025-08-14",
        "invoiceNumber": "1223"
    }
}'




{
    "message": "Transaction added successfully",
    "code": "Success",
    "amount": 100,
    "description": "zomato",
    "id": 5
}







Get All Expenses for user




Headers (for Authenticated Users):

Authorization: Bearer <JWT_TOKEN> Content-Type: application/json

curl --location 'http://localhost:8080/api/expenses/transactions' \
--header 'accept: */*' \
--header 'Authorization: ••••••' \
--header 'Cookie: JSESSIONID=56C682C168DB695C41EFDA75198E62F5'




{
    "userId": "1",
    "message": "Transactions fetched successfully",
    "transactions": [
        {
            "amount": 100.00,
            "description": "zomato",
            "id": 6
        },
        {
            "amount": 100.00,
            "description": "zomato",
            "id": 7
        }
    ]
}







Get All Expenses for user for particular expense ID




Headers (for Authenticated Users):

Authorization: Bearer <JWT_TOKEN> Content-Type: application/json

curl --location 'http://localhost:8080/api/expenses/transactions/6' \
--header 'accept: */*' \
--header 'Authorization: ••••••' \
--header 'Cookie: JSESSIONID=56C682C168DB695C41EFDA75198E62F5'




{
    "message": "Transaction fetched successfully",
    "code": "Success",
    "amount": 100.00,
    "description": "zomato",
    "id": 6
}







