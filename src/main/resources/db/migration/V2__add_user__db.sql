CREATE TYPE user_role AS ENUM ('ADMIN', 'USER', 'GUEST');

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    email VARCHAR(50),
    password VARCHAR(50),
    gender VARCHAR(50),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    about TEXT,
    age INT NOT NULL DEFAULT 0,
    role user_role
);