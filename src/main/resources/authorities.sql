SELECT *
FROM finder.authorities;
use finder;

create table `authorities`
(
    `username`    varchar(50) not null,
    `authorities` varchar(50) not null,

    unique key `authorities_idx_1` (`username`, `authorities`),
    constraint `authorities_ibfk_1`
        foreign key (`username`)
            references `users` (`username`)
) engine = InnoDB
  default charset = latin1;