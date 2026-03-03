-- Habilitar extensão pgcrypto se necessário
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Atualizar senha do usuário admin se existir
UPDATE usuario
SET senha = '$2a$12$GnVx6bB9vgyuRx6awTw9L.vjcN6ja3szvaUJGbp1FFhbVzQUY5I2C',
    updated_at = CURRENT_TIMESTAMP
WHERE email = 'admin@aluno.uece.br';
