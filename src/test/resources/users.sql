CREATE TYPE user_role AS ENUM ('ADMIN', 'USER', 'GUEST');
CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    email VARCHAR(50),
    password VARCHAR(100),
    gender gender,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    about TEXT,
    age INT NOT NULL DEFAULT 0,
    role user_role
);

INSERT INTO users (email, password, gender, firstName, lastName, about, age, role)
VALUES ('darthvader@gmail.com',
' �vX���i�G��JKJ:�Y��k]�GB��1�Y�G�*���Y��t�����Y�����s���',
'MALE', 'Darth', 'Vader', 'I''m your father', 41, 'USER');
