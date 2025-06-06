CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO dev.instituicao (id, nome, descricao, created_at, updated_at) VALUES
(uuid_generate_v4(), 'Universidade Estadual do Ceará (UECE)', 'Universidade pública estadual com sede em Fortaleza', NOW(), NOW()),
(uuid_generate_v4(), 'Universidade Federal do Ceará (UFC) - Fortaleza', 'Campus principal da UFC, instituição pública federal', NOW(), NOW()),
(uuid_generate_v4(), 'Universidade Federal do Ceará (UFC) - Crateús', 'Campus da UFC localizado em Crateús', NOW(), NOW()),
(uuid_generate_v4(), 'Universidade Federal do Ceará (UFC) - Sobral', 'Campus da UFC localizado em Sobral', NOW(), NOW()),
(uuid_generate_v4(), 'Universidade de Fortaleza (UNIFOR)', 'Universidade privada mantida pela Fundação Edson Queiroz', NOW(), NOW()),
(uuid_generate_v4(), 'Universidade Federal do Cariri (UFCA)', 'Universidade pública federal sediada em Juazeiro do Norte', NOW(), NOW()),
(uuid_generate_v4(), 'Centro Universitário Christus (UniChristus)', 'Centro universitário privado em Fortaleza', NOW(), NOW()),
(uuid_generate_v4(), 'Centro Universitário Estácio do Ceará', 'Centro universitário privado da rede Estácio', NOW(), NOW()),
(uuid_generate_v4(), 'Faculdade 7 de Setembro (FA7)', 'Faculdade privada mantida pelo Colégio 7 de Setembro', NOW(), NOW()),
(uuid_generate_v4(), 'Instituto Federal do Ceará (IFCE) - Fortaleza', 'Campus principal do IFCE, focado em educação técnica e superior', NOW(), NOW());
