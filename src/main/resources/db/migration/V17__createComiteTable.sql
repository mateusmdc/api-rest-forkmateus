CREATE TABLE comite (
    id VARCHAR(36) PRIMARY KEY,
    descricao VARCHAR(255),
    tipo INT NOT NULL CHECK (tipo BETWEEN 0 AND 3), -- 0: Gestor, 1: Usuários, 2: Técnicos, 3: Representante Discente
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