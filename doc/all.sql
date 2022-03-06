drop table if exists `test`;

create table `test`(
    `id` bigint not null comment 'ID',
    `name` varchar(50) comment 'Name',
    `password` varchar(50) comment 'password',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='Test'