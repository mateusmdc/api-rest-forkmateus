CREATE TABLE usuario (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    matricula INT NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    documento_fiscal VARCHAR(18) NOT NULL,
    foto_perfil VARCHAR(100),
    telefone VARCHAR(20),
    instituicao_id VARCHAR(36) REFERENCES instituicao(id),
    access_failed_count INT DEFAULT 0,
    lockout_enabled BOOLEAN DEFAULT FALSE,
    lockout_end TIMESTAMP,
    token_expiration TIMESTAMP,
    token_mail VARCHAR(255),
    refresh_token_enabled BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_updated_at
BEFORE UPDATE ON usuario
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();