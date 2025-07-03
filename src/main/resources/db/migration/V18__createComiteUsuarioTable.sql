CREATE TABLE comite_usuario (
    id VARCHAR(36) PRIMARY KEY,
    comite_id VARCHAR(36) NOT NULL REFERENCES comite(id) ON DELETE CASCADE,
    usuario_id VARCHAR(36) NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    espaco_id VARCHAR(36) REFERENCES espaco(id) ON DELETE SET NULL,
    descricao VARCHAR(100), -- suplente, titular, etc
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
BEFORE UPDATE ON comite_usuario
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();