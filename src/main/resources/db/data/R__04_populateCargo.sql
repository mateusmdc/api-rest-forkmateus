INSERT INTO cargo (id, nome, descricao) VALUES
('c18b204b-b9d0-4a32-b95d-227b42f4b2fc', 'ADMIN', 'Administrador do sistema com permissões totais'),
('4c826204-6f08-4c07-a3cb-469178f93db9', 'USER', 'Usuário comum do sistema'),
('c2f40cf9-265a-4a91-932f-84a7d7bc2cb9', 'ALUNO', 'Estudante da instituição'),
('4de632d0-e1b3-4ef5-8b75-b6b888deae89', 'PROFESSOR', 'Docente da instituição'),
('2c06b5f0-c31b-46d7-91ae-183902007fd5', 'TECNICO', 'Servidor técnico-administrativo'),
('dba2b2cb-4ce4-4c79-a9f0-3e75c1e88c27', 'COMISSIONADO', 'Servidor com cargo comissionado'),
('2c972ac5-50ce-489b-8b77-dab1d4de5a3e', 'PESQUISADOR', 'Usuário vinculado a projetos de pesquisa'),
('2b7d43c5-0428-4742-8894-5d7cf8f2a9e2', 'COORDENADOR', 'Usuário com responsabilidade de coordenação de curso ou setor'),
('690a2454-4d36-4e9b-8c0b-40196f0fd74d', 'SUPORTE', 'Usuário encarregado de manter a infraestrutura tecnológica'),
('8f3a5d12-9c4e-4b7a-a1f2-6e8d9c0b5a3f', 'USUARIO_EXTERNO_VISITANTE', 'Usuário externo visitante sem vínculo institucional'),
('7b2c8e45-1f9a-4d3c-b6e7-9a4f5c8d2b1e', 'SECRETARIA', 'Membro da secretaria administrativa')
ON CONFLICT (id) DO NOTHING;
