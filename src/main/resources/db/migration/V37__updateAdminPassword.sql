-- Habilitar extensão pgcrypto se necessário
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Atualizar senha do usuário admin se existir
UPDATE usuario
SET senha = crypt('@dm!n-''PPG3M''', gen_salt('bf', 10)),
    updated_at = CURRENT_TIMESTAMP
WHERE email = 'admin@aluno.uece.br';
