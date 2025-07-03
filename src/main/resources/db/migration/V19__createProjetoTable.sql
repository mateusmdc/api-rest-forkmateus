CREATE TABLE projeto (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_inicio DATE,
    data_fim DATE,
    usuario_responsavel_id VARCHAR(36) NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    instituicao_id VARCHAR(36) NOT NULL REFERENCES instituicao(id) ON DELETE CASCADE,
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

CREATE TRIGGER trigger_set_updated_at_projeto
BEFORE UPDATE ON projeto
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();
