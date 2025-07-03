CREATE TABLE comite (
    id VARCHAR(36) PRIMARY KEY,
    descricao VARCHAR(255),
    tipo INT NOT NULL CHECK (tipo BETWEEN 1 AND 4), -- 1: Gestor, 2: Usuários, 3: Técnicos, 4: Representante Discente
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

CREATE TRIGGER trigger_set_updated_at_comite
BEFORE UPDATE ON comite
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();