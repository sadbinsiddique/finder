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


# Grade Book [student, assignment_name, `course_id] (14)
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


# =====================================================
# SEED DATA - Default Roles, Permissions, and Admin User
# =====================================================

# Insert default roles
INSERT INTO `roles` (`role_name`) VALUES ('ROLE_ADMIN');
INSERT INTO `roles` (`role_name`) VALUES ('ROLE_USER');
INSERT INTO `roles` (`role_name`) VALUES ('ROLE_INSTRUCTOR');
INSERT INTO `roles` (`role_name`) VALUES ('ROLE_STUDENT');

# Insert default permissions
INSERT INTO `permissions` (`permission_name`) VALUES ('READ');
INSERT INTO `permissions` (`permission_name`) VALUES ('WRITE');
INSERT INTO `permissions` (`permission_name`) VALUES ('DELETE');
INSERT INTO `permissions` (`permission_name`) VALUES ('MANAGE_USERS');

# Assign permissions to ROLE_ADMIN (all permissions)
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (1, 1); # ADMIN -> READ
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (1, 2); # ADMIN -> WRITE
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (1, 3); # ADMIN -> DELETE
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (1, 4); # ADMIN -> MANAGE_USERS

# Assign permissions to ROLE_USER (READ only)
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (2, 1); # USER -> READ

# Assign permissions to ROLE_INSTRUCTOR (READ, WRITE)
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (3, 1); # INSTRUCTOR -> READ
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (3, 2); # INSTRUCTOR -> WRITE

# Assign permissions to ROLE_STUDENT (READ only)
INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES (4, 1); # STUDENT -> READ

# Insert default admin user
# Password: admin123 (BCrypt encoded)
INSERT INTO `users` (`username`, `enabled`, `password`)
VALUES ('admin', 1, '$2a$10$6Ql8KoKJPXmCQ6jQQ7Yme.TIqFJLJbonbGxSVqlCXHKjhC0t.DlXe');

# Assign ROLE_ADMIN to admin user
INSERT INTO `user_roles` (`username`, `role_id`) VALUES ('admin', 1);