INSERT INTO usuario (
    id,
    nome,
    email,
    documento_fiscal,
    matricula,
    senha,
    instituicao_id,
    created_at
) VALUES (
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'Admin da Silva',
    'admin@aluno.uece.br',
    '56489577825',
    123456,
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- Senha: admin (BCrypt)
    '9a379f24-abe3-49a4-b8fd-1d8a9149f3d1', -- UECE
    NOW()
) ON CONFLICT (id) DO NOTHING;