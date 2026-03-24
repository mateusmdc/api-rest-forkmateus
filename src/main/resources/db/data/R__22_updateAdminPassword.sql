CREATE EXTENSION IF NOT EXISTS pgcrypto;
UPDATE credencial_local
SET senha = '$2a$12$Zmisuy1V99.1CDSbq0IsuOycBAO2Yc1h/Syam82rGWj6X5czqRPw.',
    updated_at = CURRENT_TIMESTAMP
WHERE usuario_id = (SELECT id FROM usuario WHERE email = 'admin@aluno.uece.br');
