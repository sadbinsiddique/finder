select *
from fix_finder;



create table users
(
    username    varchar(64) unique primary key,
    password    varchar(64) not null,
    enabled     tinyint(1) default 1,
    employee_id int,
    foreign key (employee_id) references employees (id) on delete cascade
);
create table employees
(
    id            int auto_increment primary key,
    first_name    varchar(64)         not null,
    last_name     varchar(64)         not null,
    email         varchar(255) unique not null,
    date_of_barth date,
    title_id      int,
    foreign key (title_id) references job_titles (title_id) on delete set null
);
create table job_titles
(
    title_id    int auto_increment primary key,
    title_name  varchar(255) not null,
    base_income int          not null
);
create table authorities
(
    username  varchar(64),
    authority varchar(64),
    primary key (username, authority),
    foreign key (username) references users (username) on delete cascade
);
