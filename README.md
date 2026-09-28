# Fee Payment & Fee Receipt Management System

Java Swing + MySQL + JDBC. Beginner-friendly, no frameworks.

## Project structure

```
FeePaymentSystem
├── src
│   ├── Main.java                 (starts the program)
│   ├── DatabaseConnection.java   (JDBC connection + helpers)
│   ├── LoginFrame.java           (login screen)
│   ├── Dashboard.java            (main menu)
│   ├── StudentManagement.java    (student CRUD)
│   ├── FeePayment.java           (payment CRUD + calculate)
│   ├── PaymentHistory.java       (history CRUD + search)
│   └── FeeReceipt.java           (receipt + print + save)
└── database
    └── fee_payment_system.sql
```

## Step 1 — Create the database

```bash
mysql -u root -p < database/fee_payment_system.sql
```

This creates the `fee_payment_system` database, the three tables
(`users`, `students`, `payments`) and some sample rows.

**Default login:** username `admin`, password `YOUR PASSWORD HERE`

## Step 2 — Set your MySQL password

Open `src/DatabaseConnection.java` and edit these two lines:

```java
private static final String USER = "root";
private static final String PASSWORD = "root";   // <-- your MySQL password
```

## Step 3 — Download the MySQL JDBC driver

Get `mysql-connector-j-9.x.x.jar` from the MySQL website and put it in
the project folder (next to `src`).

## Step 4 — Compile and run

Linux / macOS:

```bash
javac -d build src/*.java
java -cp build:mysql-connector-j-9.1.0.jar Main
```

Windows (note the `;` instead of `:`):

```cmd
javac -d build src\*.java
java -cp build;mysql-connector-j-9.1.0.jar Main
```

In NetBeans / Eclipse / IntelliJ: create a project, put the 8 files in the
source folder, then add the connector jar to the project libraries and run
`Main.java`.

## How to use it

1. **Login** with `username` / `Your password`.
2. **Student Management** — type the details, press `ADD`. Click any table
   row to load it into the form, then `UPDATE` or `DELETE`. `SEARCH` works
   on admission number or name.
3. **Fee Payment** — press `NEW` to get a fresh receipt number, type the
   admission number and press Tab (the name, course and semester fill in
   automatically from the students table). Enter the total fee and paid
   amount, press `CALCULATE`, then `PAY / SAVE`.
4. **Payment History** — every payment in one table; select a row to edit,
   delete, or open its receipt.
5. **Fee Receipt** — `PRINT` opens the system print dialog, `SAVE` writes
   the receipt to a `.txt` file.

## Notes

- Every query uses `PreparedStatement`, so user input is safe from SQL injection.
- Every `SQLException` is caught and shown in a `JOptionPane`.
- The JTable refreshes after every Add, Update and Delete.
- `Balance = Total Fee − Paid Amount`, recalculated before every save.
- Duplicate admission numbers and receipt numbers give a clear message
  (MySQL error 1062) instead of a raw stack trace.
