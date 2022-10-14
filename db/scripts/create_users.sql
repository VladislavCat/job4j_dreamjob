CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email varchar(255) unique,
  password TEXT
);