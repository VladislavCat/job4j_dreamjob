create table POSTS (
    id serial primary key,
    name text,
    description text,
    created date,
    visible boolean,
    city_id integer
);