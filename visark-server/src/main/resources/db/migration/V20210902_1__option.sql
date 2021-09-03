create table b_option
(
    id                 varchar(32) primary key,
    create_time        datetime,
    update_time        datetime,
    timeout            bigint,
    read_timeout       bigint,
    key_deserializer   varchar(128),
    value_deserializer varchar(128)
);