SET FOREIGN_KEY_CHECKS = 0;
SET NAMES utf8mb4;

DROP SCHEMA IF EXISTS `hb-finder`;
CREATE SCHEMA `hb-finder` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `hb-finder`;

# -----------------------------------------------------
# 01. Department
# -----------------------------------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`
(
    `id`   INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 02. Role
# -----------------------------------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`
(
    `id`        INT PRIMARY KEY AUTO_INCREMENT,
    `role_name` VARCHAR(50) UNIQUE NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 03. Users
# -----------------------------------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `username` VARCHAR(50) PRIMARY KEY,
    `enabled`  BIT(1) NOT NULL DEFAULT b'1',
    `password` VARCHAR(68) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 04. User Role Mapping
# -----------------------------------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`
(
    `username` VARCHAR(50) NOT NULL,
    `role_id`  INT NOT NULL,
    PRIMARY KEY (`username`, `role_id`),
    FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 05. Permissions
# -----------------------------------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`
(
    `id`              INT PRIMARY KEY AUTO_INCREMENT,
    `permission_name` VARCHAR(50) UNIQUE NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 06. Role Permissions Mapping
# -----------------------------------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions`
(
    `role_id`       INT NOT NULL,
    `permission_id` INT NOT NULL,
    PRIMARY KEY (`role_id`, `permission_id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 07. Instructor Detail
# -----------------------------------------------------
DROP TABLE IF EXISTS `instructor_detail`;
CREATE TABLE `instructor_detail`
(
    `id`              INT PRIMARY KEY AUTO_INCREMENT,
    `age`             INT DEFAULT NULL,
    `youtube_channel` VARCHAR(128) DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 08. Instructor
# -----------------------------------------------------
DROP TABLE IF EXISTS `instructor`;
CREATE TABLE `instructor`
(
    `id`                   INT PRIMARY KEY AUTO_INCREMENT,
    `first_name`           VARCHAR(45) DEFAULT NULL,
    `last_name`            VARCHAR(45) DEFAULT NULL,
    `email`                VARCHAR(64) DEFAULT NULL,
    `instructor_detail_id` INT DEFAULT NULL,
    `username`             VARCHAR(50) DEFAULT NULL,

    KEY `FK_DETAIL_idx` (`instructor_detail_id`),
    KEY `FK_INSTRUCTOR_USER_idx` (`username`),

    CONSTRAINT `FK_DETAIL`
        FOREIGN KEY (`instructor_detail_id`)
            REFERENCES `instructor_detail` (`id`)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT `FK_INSTRUCTOR_USER`
        FOREIGN KEY (`username`)
            REFERENCES `users` (`username`)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT `UQ_INSTRUCTOR_USERNAME` UNIQUE (`username`)

) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 09. Student
# -----------------------------------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`
(
    `id`            INT PRIMARY KEY AUTO_INCREMENT,
    `first_name`    VARCHAR(45) DEFAULT NULL,
    `last_name`     VARCHAR(45) DEFAULT NULL,
    `email`         VARCHAR(64) DEFAULT NULL,
    `department_id` INT DEFAULT NULL,
    `username`      VARCHAR(50) DEFAULT NULL,

    KEY `FK_STUDENT_DEPT_idx` (`department_id`),
    KEY `FK_STUDENT_USER_idx` (`username`),

    CONSTRAINT `FK_STUDENT_DEPT`
        FOREIGN KEY (`department_id`)
            REFERENCES `department` (`id`)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT `FK_STUDENT_USER`
        FOREIGN KEY (`username`)
            REFERENCES `users` (`username`)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT `UQ_STUDENT_USERNAME` UNIQUE (`username`)

) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 10. Student Detail
# -----------------------------------------------------
DROP TABLE IF EXISTS `student_detail`;
CREATE TABLE `student_detail`
(
    `student_id`  INT PRIMARY KEY,
    `blood_group` VARCHAR(5) DEFAULT NULL,
    `address`     VARCHAR(255) DEFAULT NULL,
    FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 11. Course
# -----------------------------------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`
(
    `id`    INT PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(128) UNIQUE NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 12. Teaching Assignment
# -----------------------------------------------------
DROP TABLE IF EXISTS `teaching_assignment`;
CREATE TABLE `teaching_assignment`
(
    `instructor_id` INT NOT NULL,
    `course_id`     INT NOT NULL,
    PRIMARY KEY (`instructor_id`, `course_id`),
    FOREIGN KEY (`instructor_id`) REFERENCES `instructor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 13. Enrollment
# -----------------------------------------------------
DROP TABLE IF EXISTS `enrollment`;
CREATE TABLE `enrollment`
(
    `student_id`      INT NOT NULL,
    `course_id`       INT NOT NULL,
    `enrollment_date` DATE DEFAULT NULL,
    PRIMARY KEY (`student_id`, `course_id`),
    FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 14. Gradebook
# -----------------------------------------------------
DROP TABLE IF EXISTS `gradebook`;
CREATE TABLE `gradebook`
(
    `student_id`      INT NOT NULL,
    `course_id`       INT NOT NULL,
    `assignment_name` VARCHAR(100) NOT NULL,
    `score`           DECIMAL(5, 2) DEFAULT NULL,
    PRIMARY KEY (`student_id`, `course_id`, `assignment_name`),
    FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 15. Attendance
# -----------------------------------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`
(
    `student_id`      INT NOT NULL,
    `course_id`       INT NOT NULL,
    `attendance_date` DATE NOT NULL,
    `status`          ENUM ('Present', 'Absent', 'Late', 'Excused') NOT NULL,
    PRIMARY KEY (`student_id`, `course_id`, `attendance_date`),
    FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 16. Employee
# -----------------------------------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`
(
    `id`        INT PRIMARY KEY AUTO_INCREMENT,
    `fast_name` VARCHAR(45) DEFAULT NULL,
    `last_name` VARCHAR(45) DEFAULT NULL,
    `email`     VARCHAR(64) DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

# -----------------------------------------------------
# 17. Staff
# -----------------------------------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(45) DEFAULT NULL,
    `last_name`  VARCHAR(45) DEFAULT NULL,
    `email`      VARCHAR(64) DEFAULT NULL,
    `income`     INT DEFAULT NULL,
    `title`      VARCHAR(50) DEFAULT NULL,
    `age`        INT DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


# =====================================================
# SEED DATA - Full Population
# =====================================================

# 1. Default Roles
INSERT INTO `roles` (`id`, `role_name`) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_INSTRUCTOR'),
(4, 'ROLE_STUDENT');

# 2. Default Permissions
INSERT INTO `permissions` (`id`, `permission_name`) VALUES 
(1, 'READ'),
(2, 'WRITE'),
(3, 'DELETE'),
(4, 'MANAGE_USERS');

# 3. Role-Permission Mappings
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (1, 1), (1, 2), (1, 3), (1, 4);
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (2, 1);
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (3, 1), (3, 2);
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (4, 1);

# 4. Users (Passwords: admin -> 'admin123', others -> 'password123')
INSERT INTO `users` (`username`, `enabled`, `password`) VALUES 
('admin',       1, '$2a$10$6Ql8KoKJPXmCQ6jQQ7Yme.TIqFJLJbonbGxSVqlCXHKjhC0t.DlXe'),
('instructor1', 1, '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.XjEer.x2'),
('instructor2', 1, '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.XjEer.x2'),
('student1',    1, '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.XjEer.x2'),
('student2',    1, '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.XjEer.x2'),
('student3',    1, '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.XjEer.x2'),
('user1',       1, '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.XjEer.x2');

# 5. User-Role Assignments
INSERT INTO `user_roles` (`username`, `role_id`) VALUES 
('admin',       1),
('instructor1', 3),
('instructor2', 3),
('student1',    4),
('student2',    4),
('student3',    4),
('user1',       2);

# 6. Departments
INSERT INTO `department` (`id`, `name`) VALUES 
(1, 'Computer Science'),
(2, 'Electrical Engineering'),
(3, 'Business Administration'),
(4, 'Mathematics');

# 7. Instructor Details & Instructors
INSERT INTO `instructor_detail` (`id`, `age`, `youtube_channel`) VALUES 
(1, 38, 'CodeWithJohn'),
(2, 45, 'ProfSmithTech');

INSERT INTO `instructor` (`id`, `first_name`, `last_name`, `email`, `instructor_detail_id`, `username`) VALUES 
(1, 'John', 'Doe', 'john.doe@university.edu', 1, 'instructor1'),
(2, 'Jane', 'Smith', 'jane.smith@university.edu', 2, 'instructor2');

# 8. Students & Student Details
INSERT INTO `student` (`id`, `first_name`, `last_name`, `email`, `department_id`, `username`) VALUES 
(1, 'Alice', 'Johnson', 'alice.johnson@student.edu', 1, 'student1'),
(2, 'Bob', 'Williams', 'bob.williams@student.edu', 2, 'student2'),
(3, 'Charlie', 'Brown', 'charlie.brown@student.edu', 1, 'student3');

INSERT INTO `student_detail` (`student_id`, `blood_group`, `address`) VALUES 
(1, 'A+', '123 University Ave, Campus City'),
(2, 'O-', '456 College Rd, Townsville'),
(3, 'B+', '789 Academy St, Metro');

# 9. Courses
INSERT INTO `course` (`id`, `title`) VALUES 
(1, 'Introduction to Computer Science'),
(2, 'Data Structures and Algorithms'),
(3, 'Circuit Analysis'),
(4, 'Business Ethics');

# 10. Teaching Assignments
INSERT INTO `teaching_assignment` (`instructor_id`, `course_id`) VALUES 
(1, 1),
(1, 2),
(2, 3),
(2, 4);

# 11. Enrollments
INSERT INTO `enrollment` (`student_id`, `course_id`, `enrollment_date`) VALUES 
(1, 1, '2026-01-15'),
(1, 2, '2026-01-15'),
(2, 3, '2026-01-16'),
(3, 1, '2026-01-17'),
(3, 4, '2026-01-17');

# 12. Gradebook Entries
INSERT INTO `gradebook` (`student_id`, `course_id`, `assignment_name`, `score`) VALUES 
(1, 1, 'Midterm Exam', 92.50),
(1, 2, 'Project 1', 88.00),
(2, 3, 'Lab Assignment 1', 95.00),
(3, 1, 'Midterm Exam', 85.00);

# 13. Attendance
INSERT INTO `attendance` (`student_id`, `course_id`, `attendance_date`, `status`) VALUES 
(1, 1, '2026-02-01', 'Present'),
(1, 2, '2026-02-01', 'Present'),
(2, 3, '2026-02-01', 'Late'),
(3, 1, '2026-02-01', 'Absent');

# 14. Employees
INSERT INTO `employee` (`id`, `fast_name`, `last_name`, `email`) VALUES 
(1, 'Mark', 'Taylor', 'mark.taylor@company.com'),
(2, 'Sarah', 'Conner', 'sarah.conner@company.com');

# 15. Staff
INSERT INTO `staff` (`id`, `first_name`, `last_name`, `email`, `income`, `title`, `age`) VALUES 
(1, 'Michael', 'Scott', 'michael.scott@paper.com', 65000, 'Regional Manager', 45),
(2, 'Dwight', 'Schrute', 'dwight.schrute@paper.com', 55000, 'Assistant Regional Manager', 40);

SET FOREIGN_KEY_CHECKS = 1;