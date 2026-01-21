-- Adiciona coluna para permitir reservas de equipamentos
-- Uma solicitação pode ser de espaço OU de equipamento, nunca ambos

-- Primeiro, torna espaco_id nullable para permitir reservas de equipamento
ALTER TABLE solicitacao_reserva
ALTER COLUMN espaco_id DROP NOT NULL;

-- Adiciona coluna equipamento_id
ALTER TABLE solicitacao_reserva
ADD COLUMN equipamento_id VARCHAR(36) REFERENCES equipamento(id) ON DELETE CASCADE;

-- Adiciona constraint para garantir que apenas um tipo de reserva seja feito por solicitação
-- Ou espaco_id OU equipamento_id deve estar preenchido, mas não ambos
ALTER TABLE solicitacao_reserva
ADD CONSTRAINT chk_tipo_reserva CHECK (
    (espaco_id IS NOT NULL AND equipamento_id IS NULL) OR
    (espaco_id IS NULL AND equipamento_id IS NOT NULL)
);

-- Comentários
COMMENT ON COLUMN solicitacao_reserva.equipamento_id IS 'Referência ao equipamento reservado (exclusivo com espaco_id)';
COMMENT ON CONSTRAINT chk_tipo_reserva ON solicitacao_reserva IS 'Garante que a solicitação seja apenas de espaço OU equipamento';

-- Índice para melhorar performance de consultas por equipamento
CREATE INDEX idx_solicitacao_reserva_equipamento_id ON solicitacao_reserva(equipamento_id);
