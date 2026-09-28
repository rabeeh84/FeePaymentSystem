-- =====================================================
-- Fee Payment & Fee Receipt Management System
-- MySQL database script
-- Run this file FIRST, before running the Java program.
--
-- How to run:
--   mysql -u root -p < fee_payment_system.sql
-- =====================================================

CREATE DATABASE IF NOT EXISTS fee_payment_system;
USE fee_payment_system;

-- -----------------------------------------------------
-- Table: users  (for the login screen)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- -----------------------------------------------------
-- Table: students
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    admission_no VARCHAR(30) UNIQUE NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    course VARCHAR(50),
    semester VARCHAR(20),
    department VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100)
);

-- -----------------------------------------------------
-- Table: payments
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    receipt_no VARCHAR(50) UNIQUE NOT NULL,
    admission_no VARCHAR(30) NOT NULL,
    student_name VARCHAR(100),
    course VARCHAR(50),
    semester VARCHAR(20),
    fee_type VARCHAR(50),
    total_fee DECIMAL(10,2),
    paid_amount DECIMAL(10,2),
    balance DECIMAL(10,2),
    payment_method VARCHAR(30),
    payment_date DATE
);

-- -----------------------------------------------------
-- Sample login user
-- Username: admin      Password: your password_here
-- -----------------------------------------------------
INSERT INTO users (username, password) VALUES ('admin', 'your password_here')
    ON DUPLICATE KEY UPDATE password = 'your password_here';

-- -----------------------------------------------------
-- Sample students (so the tables are not empty on first run)
-- -----------------------------------------------------
INSERT INTO students (admission_no, student_name, course, semester, department, phone, email)
VALUES
 ('ADM1001', 'Arjun Menon',   'B.Tech', 'S3', 'Computer Science', '9876543210', 'arjun@example.com'),
 ('ADM1002', 'Fathima Rashid','B.Tech', 'S5', 'Electronics',      '9876501234', 'fathima@example.com'),
 ('ADM1003', 'Rahul Nair',    'MCA',    'S1', 'Computer Science', '9847012345', 'rahul@example.com')
ON DUPLICATE KEY UPDATE student_name = VALUES(student_name);

-- -----------------------------------------------------
-- Sample payment
-- -----------------------------------------------------
INSERT INTO payments (receipt_no, admission_no, student_name, course, semester,
                      fee_type, total_fee, paid_amount, balance, payment_method, payment_date)
VALUES ('RCPT1001', 'ADM1001', 'Arjun Menon', 'B.Tech', 'S3',
        'Tuition Fee', 50000.00, 20000.00, 30000.00, 'UPI', CURDATE())
ON DUPLICATE KEY UPDATE paid_amount = VALUES(paid_amount);
