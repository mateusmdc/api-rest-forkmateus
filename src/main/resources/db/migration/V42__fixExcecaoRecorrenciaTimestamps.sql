-- Corrige as colunas de auditoria da tabela excecao_recorrencia,
-- adicionando defaults e tornando created_at NOT NULL (consistente com demais tabelas).
--
-- 1. Preenche NULLs existentes antes de aplicar NOT NULL em created_at.
-- 2. Torna created_at NOT NULL com DEFAULT CURRENT_TIMESTAMP.
-- 3. Define updated_at com DEFAULT CURRENT_TIMESTAMP e atualização automática.

UPDATE excecao_recorrencia
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;

UPDATE excecao_recorrencia
SET updated_at = CURRENT_TIMESTAMP
WHERE updated_at IS NULL;

ALTER TABLE excecao_recorrencia
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN updated_at SET NOT NULL,
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;
