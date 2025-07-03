-- Comitê Gestor Geral (sem espaco_id)
INSERT INTO comite_usuario (
    id, comite_id, usuario_id, espaco_id, descricao
) VALUES (
    'b1c48261-9d63-4a44-9a8f-0ef3df260301',
    '5e6fcbf4-b8d2-4c27-8d19-56efc7bcb479', -- Comitê Gestor Geral
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a', -- Usuário
    NULL,
    'Titular'
);

-- Comitê de Usuários - Ciências da Computação
INSERT INTO comite_usuario (
    id, comite_id, usuario_id, espaco_id, descricao
) VALUES (
    '9c580cb2-bd53-490a-b21d-8c712e47f06d',
    '25761ec1-d328-4a25-b737-985994563a2e', -- Comitê de Usuários - Ciências da Computação
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'cc20b6e6-dc56-4db6-92fa-df47c99961bb',
    'Titular'
);

-- Técnicos Associados
INSERT INTO comite_usuario (
    id, comite_id, usuario_id, espaco_id, descricao
) VALUES (
    '7a0036c9-3242-4c3f-9517-59d348b7a41f',
    'af7b2176-4e1e-46bc-9e91-cb8245a70157',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'cc20b6e6-dc56-4db6-92fa-df47c99961bb',
    'Técnico Administrativo'
);

-- Representante Discente da Pós-Graduação
INSERT INTO comite_usuario (
    id, comite_id, usuario_id, espaco_id, descricao
) VALUES (
    'd8f365b5-3f56-46f9-96c2-b06e90aa3f55',
    'd2a2f97b-2b1b-4451-a218-c61d9ae72e70',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'cc20b6e6-dc56-4db6-92fa-df47c99961bb',
    'Representante Discente'
);