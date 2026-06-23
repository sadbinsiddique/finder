SELECT *
FROM finder.employee;
use finder;

create table `users`
(
    `username` varchar(50) not null,
    `password` varchar(50) not null,
    `enabled`  tinyint     not null,

    primary key (`username`)
) engine = InnoDB
  default charset = latin1;

insert into `users` value
    ('john', '{noop}1234', 1),
    ('mary', '{noop}1234', 1),
    ('susan', '{noop}1234', 1);