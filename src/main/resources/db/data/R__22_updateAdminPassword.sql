-- Habilitar extensão pgcrypto se necessário
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Atualizar senha do usuário admin se existir
UPDATE usuario
SET senha = '$2a$12$Zmisuy1V99.1CDSbq0IsuOycBAO2Yc1h/Syam82rGWj6X5czqRPw.',
    updated_at = CURRENT_TIMESTAMP
WHERE email = 'admin@aluno.uece.br';
