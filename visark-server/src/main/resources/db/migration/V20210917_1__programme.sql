create table b_programme
(
    id            varchar(32) primary key,
    create_time   datetime,
    update_time   datetime,
    cluster_id    varchar(32),
    topic         varchar(128),
    content_key   text,
    content_value text,
    expressions   text
);