-- Adiciona campos de recorrência à tabela solicitacao_reserva
-- Permite que reservas sejam configuradas para se repetirem em intervalos específicos

-- tipo_recorrencia: 0=Não repete, 1=Diária, 2=Semanal, 3=Mensal
ALTER TABLE solicitacao_reserva 
ADD COLUMN tipo_recorrencia INT NOT NULL DEFAULT 0;

-- data_fim_recorrencia: data até quando a recorrência deve se repetir (opcional)
ALTER TABLE solicitacao_reserva 
ADD COLUMN data_fim_recorrencia TIMESTAMP;

-- reserva_pai_id: referência à reserva original (reserva mãe) em caso de recorrência
-- Permite rastrear todas as reservas geradas a partir de uma solicitação recorrente
ALTER TABLE solicitacao_reserva 
ADD COLUMN reserva_pai_id VARCHAR(36);

-- Adiciona índice para melhorar performance de consultas por reserva pai
CREATE INDEX idx_solicitacao_reserva_pai ON solicitacao_reserva(reserva_pai_id);

-- Adiciona índice para melhorar performance de consultas por tipo de recorrência
CREATE INDEX idx_solicitacao_tipo_recorrencia ON solicitacao_reserva(tipo_recorrencia);
