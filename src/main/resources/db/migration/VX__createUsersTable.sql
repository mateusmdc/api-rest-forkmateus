CREATE TABLE users (
    id UUID PRIMARY KEY,
    login VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    cpf VARCHAR(14) UNIQUE,
    role VARCHAR(100) NOT NULL,
    phone VARCHAR(14),
    birthday DATE,
    token_expiration TIMESTAMP,
    token_mail VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);