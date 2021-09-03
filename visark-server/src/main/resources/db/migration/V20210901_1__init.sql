create table b_cluster
(
    id                varchar(32) primary key,
    name              varchar(128),
    bootstrap_servers varchar(1024),
    create_time       datetime,
    update_time       datetime,
    description       text
);