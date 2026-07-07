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

  ┌───────────┬────────────┐
  │   Layer   │ Technology │
  ├───────────┼────────────┤
  │ Language  │ Java 8     │
  ├───────────┼────────────┤
  │ GUI       │ Swing      │
  ├───────────┼────────────┤
  │ Database  │ MySQL 8.0  │
  ├───────────┼────────────┤
  │ DB Access │ JDBC       │
  ├───────────┼────────────┤
  │ Build     │ Maven      │
  └───────────┴────────────┘

  Project Structure

  src/project/
  ├── Login.java            # Login window
  ├── AtmFrame.java         # Main dashboard
  ├── Atm.java              # CLI entry & shared state
  ├── User.java             # User entity
  ├── Transaction.java      # Transaction entity
  ├── dao/UserDao.java      # Database operations
  └── util/DBUtil.java      # JDBC connection utility

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
  
