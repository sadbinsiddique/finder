drop schema if exists `hb-finder`;

create schema `hb-finder`;

use `hb-finder`;

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

drop table if exists `instructor`;

create table `instructor`
(
    `id`                   int(11) not null auto_increment,
    `first_name`           varchar(45) default null,
    `last_name`            varchar(45) default null,
    `email`                varchar(64) default null,
    `instructor_detail_id` int(11)     default null,
    primary key (`id`),
    key `FK_DETAIL_idx` (`instructor_detail_id`),
    constraint `FK_DETAIL` foreign key (`instructor_detail_id`) references `instructor_detail` (`id`) on delete no
        action on update no action
) engine = InnoDB
  auto_increment = 1
  default charset = latin1;

set foreign_key_checks = 1;