# Description:
This project is a Spring Boot application for managing employee records. It provides endpoints to perform CRUD operations on employee data. 

# Technologies Used:
- Java
- Spring Boot
- Maven
- CouchBase Capella

# Prerequisites:
- JDK 17 or higher
- CouchBase Capella setup (Already Setup)
- Maven

# How to Run the Project:

Method 1:
1. The Folder contains java project.
2. Open the project in your preferred IDE.
3. Set up the database configurations in the application.properties file.
4. Run the main application class (EmploywiseApplication.java) to start the Spring Boot application.
5. Access the API endpoints using the base URL.

Method 2:
1. The JAR file is included in the folder.
2. Run the jar using: java -jar employwise.jar

# API Documentation:

1. Get Employee by ID
   - Method: GET
   - Endpoint: /api/employee/{id}
   - Description: Retrieves the details of an employee by their ID.
   - Input JSON Structure: N/A

2. Add Employee
   - Method: POST
   - Endpoint: /api/employee/add
   - Description: Adds a new employee to the database.
   - Input JSON Structure: 
     {
       "employeeName": "John Doe",
       "phoneNumber": "1234567890",
       "email": "john.doe@example.com",
       "reportsTo": "managerId",
       "profileImage": "image-url"
     }

3. Get All Employees (With Pagination and Sorting)
   - Method: GET
   - Endpoint: api/employee/all?pageNumber={pageNumber}&sortBy={sortBy}
   - Description: Retrieves all employees with pagination and sorting options.
   - Input JSON Structure: N/A

4. Get All Employees (Alternate)
   - Method: GET
   - Endpoint: /api/employee/get/all
   - Description: Retrieves all employees without pagination and sorting.
   - Input JSON Structure: N/A

5. Delete Employee by ID
   - Method: DELETE
   - Endpoint: /api/employee/delete/{id}
   - Description: Deletes an employee by their ID.
   - Input JSON Structure: N/A

6. Update Employee by ID
   - Method: PUT
   - Endpoint: /api/employee/update/{id}
   - Description: Updates the details of an employee by their ID.
   - Input JSON Structure: 
     {
       "employeeName": "John Doe",
       "phoneNumber": "1234567890",
       "email": "john.doe@example.com",
       "reportsTo": "managerId",
       "profileImage": "image-url"
     }

7. Get nth Level Manager
   - Method: GET
   - Endpoint: /api/employee/{employeeId}/manager?level={level}
   - Description: Retrieves the nth level manager of an employee.
   - Input JSON Structure: N/A

