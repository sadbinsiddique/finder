drop schema if exists `hb-finder`;
create schema `hb-finder`;
use `hb-finder`;

# Department (01)
drop table if exists `department`;
CREATE TABLE `department`
(
    `id`   INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL
) ENGINE = InnoDB;


# Role (02)
drop table if exists `roles`;
CREATE TABLE `roles`
(
    `id`        INT PRIMARY KEY AUTO_INCREMENT,
    `role_name` VARCHAR(50) UNIQUE
) ENGINE = InnoDB;

# Users (03)
drop table if exists `users`;
CREATE TABLE `users`
(
    `username` VARCHAR(50) PRIMARY KEY,
    `enabled`  BIT(1),
    `password` VARCHAR(68)
) ENGINE = InnoDB;

# User Role [user][role] (04)
drop table if exists `user_roles`;
CREATE TABLE `user_roles`
(
    `username` VARCHAR(50),
    `role_id`  INT,
    PRIMARY KEY (`username`, `role_id`),
    FOREIGN KEY (`username`) REFERENCES `users` (`username`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB;

# Permissions (05)
drop table if exists `permissions`;
CREATE TABLE `permissions`
(
    `id`              INT PRIMARY KEY AUTO_INCREMENT,
    `permission_name` VARCHAR(50) UNIQUE
) ENGINE = InnoDB;

# Role Permissions [permissions] [role] (06)
drop table if exists `role_permissions`;
CREATE TABLE `role_permissions`
(
    `role_id`       INT,
    `permission_id` INT,
    PRIMARY KEY (`role_id`, `permission_id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
    FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
) ENGINE = InnoDB;

# Instructor Detail (07)
drop table if exists `instructor_detail`;
create table `instructor_detail`
(
    `id`              int(11) not null auto_increment,
    `age`             int(3)       default null,
    `youtube_channel` varchar(128) default null,
    primary key (`id`)
) engine = InnoDB
  auto_increment = 1
  default charset = latin1;

# Instructor [instructor_detail] (08)
drop table if exists `instructor`;
create table `instructor`
(
    `id`                   int(11) not null auto_increment,
    `first_name`           varchar(45) default null,
    `last_name`            varchar(45) default null,
    `email`                varchar(64) default null,
    `instructor_detail_id` int(11)     default null,
    `username`             varchar(50) default null,

    primary key (`id`),

    key `FK_DETAIL_idx` (`instructor_detail_id`),
    key `FK_INSTRUCTOR_USER_idx` (`username`),

    constraint `FK_DETAIL`
        foreign key (`instructor_detail_id`)
            references `instructor_detail` (`id`)
            on delete no action on update no action,

    constraint `FK_INSTRUCTOR_USER`
        foreign key (`username`)
            references `users` (`username`)
            on delete no action on update no action,

    constraint `UQ_INSTRUCTOR_USERNAME` unique (`username`)

) engine = InnoDB
  auto_increment = 1
  default charset = latin1;

# Student [department] (09)
drop table if exists `student`;
create table `student`
(
    `id`            int not null auto_increment,
    `first_name`    varchar(45) default null,
    `last_name`     varchar(45) default null,
    `email`         varchar(64) default null,
    `department_id` int         default null,
    `username`      varchar(50) default null,

    primary key (`id`),

    key `FK_STUDENT_DEPT_idx` (`department_id`),
    key `FK_STUDENT_USER_idx` (`username`),

    constraint `FK_STUDENT_DEPT`
        foreign key (`department_id`)
            references `department` (`id`)
            on delete no action on update no action,

    constraint `FK_STUDENT_USER`
        foreign key (`username`)
            references `users` (`username`)
            on delete no action on update no action,

    constraint `UQ_STUDENT_USERNAME` unique (`username`)

) engine = InnoDB
  auto_increment = 1
  default charset = latin1;

# Student Detail [student] (10)
drop table if exists `student_detail`;
CREATE TABLE `student_detail`
(
    `student_id`  INT PRIMARY KEY,
    `blood_group` VARCHAR(5),
    `address`     VARCHAR(255),
    FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE = InnoDB;

# Course (11)
drop table if exists `course`;
CREATE TABLE `course`
(
    `id`    INT PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(128) UNIQUE
) ENGINE = InnoDB;

# Teaching Assignment [instructor][course] (12)
drop table if exists `teaching_assignment`;
CREATE TABLE `teaching_assignment`
(
    `instructor_id` INT,
    `course_id`     INT,
    PRIMARY KEY (`instructor_id`, `course_id`),
    FOREIGN KEY (`instructor_id`) REFERENCES `instructor` (`id`),
    FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE = InnoDB;

# Enrollment [student][course] (13)
drop table if exists `enrollment`;
CREATE TABLE `enrollment`
(
    `student_id`      INT,
    `course_id`       INT,
    `enrollment_date` DATE,
    PRIMARY KEY (`student_id`, `course_id`),
    FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
    FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE = InnoDB;

# Grade Book [student, assignment_name, course_id] (14)
drop table if exists `gradebook`;
CREATE TABLE `gradebook`
(
    `student_id`      INT,
    `course_id`       INT,
    `assignment_name` VARCHAR(100),
    `score`           DECIMAL(5, 2),
    PRIMARY KEY (`student_id`, `course_id`, `assignment_name`),
    FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
    FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE = InnoDB;

# Attendance  [student, course_id] (15)
drop table if exists `attendance`;
CREATE TABLE `attendance`
(
    `student_id`      INT,
    `course_id`       INT,
    `attendance_date` DATE,
    `status`          ENUM ('Present', 'Absent', 'Late', 'Excused'),
    PRIMARY KEY (`student_id`, `course_id`, `attendance_date`),
    FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
    FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE = InnoDB;

# Employee (16)
drop table if exists `employee`;
CREATE TABLE `employee`
(
    `id`        INT PRIMARY KEY AUTO_INCREMENT,
    `fast_name` VARCHAR(45) DEFAULT NULL,
    `last_name` VARCHAR(45) DEFAULT NULL,
    `email`     VARCHAR(64) DEFAULT NULL
) ENGINE = InnoDB;

# Staff (17)
drop table if exists `staff`;
CREATE TABLE `staff`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(45) DEFAULT NULL,
    `last_name`  VARCHAR(45) DEFAULT NULL,
    `email`      VARCHAR(64) DEFAULT NULL,
    `income`     INT         DEFAULT NULL,
    `title`      VARCHAR(50) DEFAULT NULL,
    `age`        INT         DEFAULT NULL
) ENGINE = InnoDB;


# =====================================================
# SEED DATA - Full Sample Database Population
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
# ADMIN -> All permissions
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (1, 1), (1, 2), (1, 3), (1, 4);
# USER -> READ
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (2, 1);
# INSTRUCTOR -> READ, WRITE
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (3, 1), (3, 2);
# STUDENT -> READ
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (4, 1);

# 4. Users (Passwords: admin -> 'admin123', others -> 'password123')
# BCrypt hashes:
# admin123:   $2a$10$6Ql8KoKJPXmCQ6jQQ7Yme.TIqFJLJbonbGxSVqlCXHKjhC0t.DlXe
# password123: $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.XjEer.x2
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
('admin',       1), # ADMIN
('instructor1', 3), # INSTRUCTOR
('instructor2', 3), # INSTRUCTOR
('student1',    4), # STUDENT
('student2',    4), # STUDENT
('student3',    4), # STUDENT
('user1',       2); # USER

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