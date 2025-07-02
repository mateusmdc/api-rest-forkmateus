CREATE TABLE espaco (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    url_cnpq VARCHAR(255),
    observacao VARCHAR(255),
    departamento_id VARCHAR(36) REFERENCES departamento(id) ON DELETE CASCADE,
    localizacao_id VARCHAR(36) REFERENCES localizacao(id) ON DELETE CASCADE,
    tipo_espaco_id VARCHAR(36) REFERENCES tipo_espaco(id) ON DELETE CASCADE,
    precisa_projeto BOOLEAN NOT NULL DEFAULT FALSE,
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

CREATE TRIGGER trigger_set_updated_at_espaco
BEFORE UPDATE ON espaco
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();
