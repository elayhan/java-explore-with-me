create table if not exists endpoint_hit (
    id bigint generated always as identity primary key,
    app varchar(255) not null,
    uri varchar(255) not null,
    ip varchar(255) not null,
    timestamp timestamp without time zone
);
