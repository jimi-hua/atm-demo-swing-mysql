ATM Banking System

  A Java Swing desktop ATM simulation application with MySQL database.

  Features

  - Login — Secure authentication with username & password
  - Balance Inquiry — View account balance and status
  - Deposit / Withdrawal — Cash in and out with validation
  - Fund Transfer — Transfer money between accounts
  - Change Password — Update password with old password verification
  - Password Recovery — Retrieve forgotten password via ID card
  - Open Account — Register new bank account
  - Report Loss — Freeze account with identity verification
  - Transaction History — View recent transaction records

  Tech Stack
  Tech Stack: This project uses Java 8 as the development language, adopts Swing to build graphical user interfaces, stores business data with MySQL 8.0, implements database access via JDBC, and leverages Maven for project building and dependency management.
  
  Quick Start

  1. Setup Database

  mysql -u root -p < init.sql

  2. Configure Connection

  cp src/db.properties.example src/db.properties

  Then edit src/db.properties with your MySQL username and password.

  3. Run

  Open the project in IntelliJ IDEA, wait for Maven dependencies, then run Login.main().

  Or via command line:

  mvn compile exec:java -Dexec.mainClass="project.Login"

  This is a pure course training & personal practice project, for learning purposes only.
  
