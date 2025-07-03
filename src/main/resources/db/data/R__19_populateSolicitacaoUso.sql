-- Solicitação sem projeto associado
INSERT INTO solicitacao_reserva (
    id, data_inicio, data_fim, espaco_id, usuario_solicitante_id, status, projeto_id, created_at
) VALUES (
    '1a3f36d9-b5b8-44e3-8e2e-0a8e7b2dcd01',
    '2025-07-05 08:00:00',
    '2025-07-05 10:00:00',
    'cc20b6e6-dc56-4db6-92fa-df47c99961bb',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    0,
    NULL,
    NOW()
);

-- Solicitação com projeto associado
INSERT INTO solicitacao_reserva (
    id, data_inicio, data_fim, espaco_id, usuario_solicitante_id, status, projeto_id, created_at
) VALUES (
    '7b4e3a7a-f3b2-4d49-bc5d-e6b8dfec355e',
    '2025-07-10 14:00:00',
    '2025-07-10 16:00:00',
    'cc20b6e6-dc56-4db6-92fa-df47c99961bb',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    1,
    'a1f5d8e2-7d3b-4e1a-b45f-cd2a6b8f5f01',
    NOW()
);
