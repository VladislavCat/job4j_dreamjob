create table candidates (
    id serial primary key,
    name text,
    description text,
    created date,
    photo integer[]
);