INSERT INTO usuario (
    id,
    nome,
    email,
    matricula,
    senha,
    instituicao_id,
    created_at
) VALUES (
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'Admin da Silva',
    'admin@aluno.uece.br',
    123456,
    'admin',
    '9a379f24-abe3-49a4-b8fd-1d8a9149f3d1', -- UECE
    NOW()
);