-- Adiciona a coluna 'is_serie' na tabela solicitacao_reserva.
-- Quando TRUE, indica que o registro é uma série recorrente no novo modelo:
--   - apenas 1 registro é criado por série, independentemente do número de ocorrências.
-- Registros antigos (criados antes desta migração) mantêm o valor FALSE,
--   preservando a compatibilidade com o modelo anterior.
ALTER TABLE solicitacao_reserva
    ADD COLUMN is_serie BOOLEAN NOT NULL DEFAULT FALSE;
