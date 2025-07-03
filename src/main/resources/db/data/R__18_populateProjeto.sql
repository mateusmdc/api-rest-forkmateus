INSERT INTO projeto (
    id, nome, descricao, data_inicio, data_fim, usuario_responsavel_id, instituicao_id, created_at
) VALUES
(
    'a1f5d8e2-7d3b-4e1a-b45f-cd2a6b8f5f01',
    'Projeto de Pesquisa em IA',
    'Pesquisa sobre algoritmos de inteligência artificial aplicados a sistemas de recomendação.',
    '2024-01-15',
    '2025-01-15',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    '9a379f24-abe3-49a4-b8fd-1d8a9149f3d1',
    NOW()
),
(
    'b3e7f402-9a54-48b6-b334-8a49b4b7f902',
    'Desenvolvimento de Software Educacional',
    'Projeto focado na criação de software para educação a distância.',
    '2023-07-01',
    '2024-12-31',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    '13b40e17-1bb3-421e-817e-32bde294e8e2',
    NOW()
),
(
    'd9b1f33a-2f16-4d1e-8142-44d799a0e903',
    'Monitoramento Ambiental',
    'Projeto de monitoramento ambiental utilizando sensores IoT em Crateús.',
    '2023-09-01',
    '2024-09-01',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'fb4bb452-79cc-4be6-a06b-86e801fc2639',
    NOW()
);