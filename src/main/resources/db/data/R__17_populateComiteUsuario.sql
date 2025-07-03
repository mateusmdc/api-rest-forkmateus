INSERT INTO comite_usuario (
    id, comite_id, usuario_id, departamento_id, descricao, portaria, is_titular
) VALUES (
    'b1c48261-9d63-4a44-9a8f-0ef3df260301',
    '5e6fcbf4-b8d2-4c27-8d19-56efc7bcb479', -- Comitê Gestor
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a', -- Usuário
    'da3d46d7-f131-4bbd-84bd-043df2b7ce35',
    'Membro Titular do comitê gestor de ciencias da computação',
    '565657',
    TRUE
);

-- Comitê de Usuários - Ciências da Computação
INSERT INTO comite_usuario (
    id, comite_id, usuario_id, departamento_id, descricao, portaria, is_titular
) VALUES (
    '9c580cb2-bd53-490a-b21d-8c712e47f06d',
    '25761ec1-d328-4a25-b737-985994563a2e', -- Comitê de Usuários - Ciências da Computação
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'da3d46d7-f131-4bbd-84bd-043df2b7ce35',
    'Membro do Comitê de Usuários',
    '11303949',
    FALSE
);

-- Técnicos Associados
INSERT INTO comite_usuario (
    id, comite_id, usuario_id, departamento_id, descricao, portaria, is_titular
) VALUES (
    '7a0036c9-3242-4c3f-9517-59d348b7a41f',
    'af7b2176-4e1e-46bc-9e91-cb8245a70157',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'da3d46d7-f131-4bbd-84bd-043df2b7ce35',
    'Técnico Administrativo',
    '1505051',
    TRUE
);

-- Representante Discente da Pós-Graduação
INSERT INTO comite_usuario (
    id, comite_id, usuario_id, departamento_id, descricao, portaria, is_titular
) VALUES (
    'd8f365b5-3f56-46f9-96c2-b06e90aa3f55',
    'd2a2f97b-2b1b-4451-a218-c61d9ae72e70',
    'c5a0e1e1-4d9f-4ddf-85de-546d1471708a',
    'da3d46d7-f131-4bbd-84bd-043df2b7ce35',
    'Representante Discente Titular da Pós Graduação',
    '4',
    TRUE
);