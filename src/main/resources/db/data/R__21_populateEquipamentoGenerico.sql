-- Limpar dados antigos para repopular
DELETE FROM equipamento_generico;

-- Popular tabela de equipamentos genéricos
-- Equipamentos genéricos são itens utilizados em grande quantidade em espaços
INSERT INTO equipamento_generico (id, nome, created_at, updated_at) VALUES
('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', 'Cadeira', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'Lousa', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', 'Lixeira', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', 'Apagador', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
