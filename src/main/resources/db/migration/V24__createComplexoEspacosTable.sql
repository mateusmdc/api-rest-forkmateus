CREATE TABLE complexo_espacos (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    site VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
