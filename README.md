# 🖥️ User and Machine Management System

Welcome to the User and Machine Management System! This project is a full-stack application built to manage users and machines with extensive CRUD functionalities and robust authentication and authorization features.

## Features

✅ **User Management**:
   - Create, Read, Update, and Delete (CRUD) operations for users.
   - User authentication and authorization using JWT tokens.

✅ **Machine Management**:
   - CRUD operations for machines.
   - Machines can be RUN, STOPPED, DISCHARGED, and scheduled to run at specified times.
   - Transaction and pessimistic locking used for scheduling machines to ensure data integrity.

✅ **Permissions and Roles**:
   - Users have permissions regarding user and machine management based on their roles.
   - Full authentication and authorization with JWT tokens on the backend.

✅ **Search Functionality**:
   - Search functionality with various parameters for both users and machines.

✅ **Error Handling**:
   - All possible errors related to running, stopping, discharging, and scheduling machines are handled gracefully.


## Technologies Used

- **Frontend**: Angular
- **Backend**: Java (Spring Boot), Hibernate JPA
- **Database**: MySQL
- **Authentication**: JWT Token

