CREATE TABLE solicitacao_reserva (
    id VARCHAR(36) PRIMARY KEY,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    espaco_id VARCHAR(36) NOT NULL REFERENCES espaco(id) ON DELETE CASCADE,
    usuario_solicitante_id VARCHAR(36) NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    status INT NOT NULL, -- 0=Pendente, 1=Aprovado, 2=Recusado, 3=Pendente de Ajuste
    projeto_id VARCHAR(36) REFERENCES projeto(id) ON DELETE SET NULL,
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

CREATE TRIGGER trigger_set_updated_at_solicitacao_reserva
BEFORE UPDATE ON solicitacao_reserva
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();
