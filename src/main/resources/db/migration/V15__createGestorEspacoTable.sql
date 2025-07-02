CREATE TABLE gestor_espaco (
    id VARCHAR(36) PRIMARY KEY,
    usuario_gestor_id VARCHAR(36) REFERENCES usuario(id) ON DELETE CASCADE,
    espaco_id VARCHAR(36) REFERENCES espaco(id) ON DELETE CASCADE,
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
BEFORE UPDATE ON gestor_espaco
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();
