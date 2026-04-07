INSERT INTO usuario (
    id,
    nome,
    email,
    documento_fiscal,
    matricula,
    instituicao_id,
    created_at
) VALUES (
             'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
             'Admin da Silva',
             'admin@aluno.uece.br',
             '56489577825',
             '123456',
             '9a379f24-abe3-49a4-b8fd-1d8a9149f3d1',
             NOW()
         ) ON CONFLICT (id) DO NOTHING;

INSERT INTO credencial_local (
    id,
    usuario_id,
    senha,
    created_at,
    updated_at
) VALUES (
             'd8f7e6c5-4b3a-2109-8765-4321fedcba09',
             'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
             '$2a$12$vDmU1Y8jaeYAGaSdk.aiaePOVIb0wWKYq4Fgg/zsnQ2MVofJmF9ya',
             NOW(),
             NOW()
         ) ON CONFLICT (usuario_id) DO UPDATE SET senha = EXCLUDED.senha;