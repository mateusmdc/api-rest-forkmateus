-- Tabela para vincular usuários da secretaria aos espaços
CREATE TABLE secretaria_espaco (
    id VARCHAR(36) PRIMARY KEY,
    usuario_secretaria_id VARCHAR(36) NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    espaco_id VARCHAR(36) NOT NULL REFERENCES espaco(id) ON DELETE CASCADE,
    esta_ativo BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Função para atualizar o campo deleted_at quando o registro for inativado
CREATE OR REPLACE FUNCTION atualizar_deleted_at_secretaria_espaco()
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

-- Trigger para executar a função antes de atualizar o registro
CREATE TRIGGER trigger_atualizar_deleted_at_secretaria_espaco
BEFORE UPDATE ON secretaria_espaco
FOR EACH ROW
WHEN (OLD.esta_ativo IS DISTINCT FROM NEW.esta_ativo)
EXECUTE FUNCTION atualizar_deleted_at_secretaria_espaco();
