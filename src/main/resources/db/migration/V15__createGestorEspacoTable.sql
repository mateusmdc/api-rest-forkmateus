CREATE TABLE gestor_espaco (
    id VARCHAR(36) PRIMARY KEY,
    usuario_gestor_id VARCHAR(36) REFERENCES usuario(id) ON DELETE CASCADE,
    espaco_id VARCHAR(36) REFERENCES espaco(id) ON DELETE CASCADE,
    esta_ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE OR REPLACE FUNCTION atualizar_deleted_at_quando_inativo()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.esta_ativo = FALSE THEN
        NEW.deleted_at := CURRENT_TIMESTAMP;
    ELSE
        NEW.deleted_at := NULL;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_deleted_at
BEFORE UPDATE ON gestor_espaco
FOR EACH ROW
WHEN (OLD.esta_ativo IS DISTINCT FROM NEW.esta_ativo)
EXECUTE FUNCTION atualizar_deleted_at_quando_inativo();