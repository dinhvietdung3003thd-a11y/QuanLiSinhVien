CREATE DATABASE IF NOT EXISTS quanlysinhvien;
USE quanlysinhvien;

-- Bảng Users (Tài khoản)
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'admin' hoặc 'student'
);

-- Bảng Students (Sinh viên)
CREATE TABLE students (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100)
);

-- Bảng Subjects (Môn học)
CREATE TABLE subjects (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    credits INT
);

-- Bảng Grades (Điểm) - Khóa chính phức hợp (student_id + subject_id)
CREATE TABLE grades (
    student_id VARCHAR(20),
    subject_id VARCHAR(20),
    score DOUBLE,
    PRIMARY KEY (student_id, subject_id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- Thêm 1 tài khoản admin mặc định
INSERT INTO users VALUES ('admin', '123', 'admin');