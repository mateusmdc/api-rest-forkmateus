-- Migration para migrar dados existentes e remover coluna tipo_atividade_id da tabela espaco
-- Esta migration preserva os dados existentes movendo-os para a nova tabela de relacionamento

-- Migrar dados existentes da coluna tipo_atividade_id para a tabela de relacionamento
-- Apenas migra registros onde tipo_atividade_id não é nulo
INSERT INTO espaco_tipo_atividade (espaco_id, tipo_atividade_id)
SELECT id, tipo_atividade_id
FROM espaco
WHERE tipo_atividade_id IS NOT NULL;

-- Remover a constraint de foreign key antiga
ALTER TABLE espaco DROP CONSTRAINT IF EXISTS espaco_tipo_atividade_id_fkey;

-- Remover a coluna tipo_atividade_id da tabela espaco
ALTER TABLE espaco DROP COLUMN tipo_atividade_id;
