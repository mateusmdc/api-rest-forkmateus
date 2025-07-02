CREATE TABLE equipamento (
    id VARCHAR(36) PRIMARY KEY,
    tombamento VARCHAR (100) NOT NULL UNIQUE,
    descricao VARCHAR (255),
    status INT CHECK (status IN (0, 1, 2)) NOT NULL DEFAULT 1,
    tipo_equipamento_id VARCHAR(36) REFERENCES tipo_equipamento(id) ON DELETE CASCADE,
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
BEFORE UPDATE ON equipamento
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();