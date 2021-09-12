create table b_topic_option
(
    id                 varchar(32) primary key,
    create_time        datetime,
    update_time        datetime,
    cluster_id         varchar(32),
    topic              varchar(128),
    key_deserializer   varchar(128),
    value_deserializer varchar(128)
);