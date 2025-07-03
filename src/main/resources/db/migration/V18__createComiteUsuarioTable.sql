CREATE TABLE comite_usuario (
    id VARCHAR(36) PRIMARY KEY,
    comite_id VARCHAR(36) NOT NULL REFERENCES comite(id) ON DELETE CASCADE,
    usuario_id VARCHAR(36) NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    departamento_id VARCHAR(36) REFERENCES departamento(id) ON DELETE SET NULL,
    descricao VARCHAR(100),
    portaria VARCHAR(50),
    is_titular BOOLEAN DEFAULT FALSE,
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