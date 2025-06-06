CREATE TABLE curso (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    departamento_id VARCHAR(36) REFERENCES departamento(id),
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
BEFORE UPDATE ON curso
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();