-- Limpar cargos antigos que não fazem mais parte do modelo de negócio
DELETE FROM cargo WHERE nome NOT IN ('ADMIN', 'USUARIO_INTERNO', 'USUARIO_EXTERNO');

-- Inserir os três tipos de usuário do novo modelo de negócio
INSERT INTO cargo (id, nome, descricao) VALUES
('c18b204b-b9d0-4a32-b95d-227b42f4b2fc', 'ADMIN', 'Administrador do sistema com permissões totais'),
('8f3a5d12-9c4e-4b7a-a1f2-6e8d9c0b5a3f', 'USUARIO_INTERNO', 'Usuário com vínculo institucional (aluno, professor, técnico, etc.)'),
('7b2c8e45-1f9a-4d3c-b6e7-9a4f5c8d2b1e', 'USUARIO_EXTERNO', 'Usuário externo sem vínculo institucional')
ON CONFLICT (id) DO NOTHING;
