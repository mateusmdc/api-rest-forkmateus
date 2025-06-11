CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO curso (id, nome, departamento_id, created_at, updated_at) VALUES
(uuid_generate_v4(), 'Medicina', '25ac7d29-1576-419f-8baf-7983cd39f932', NOW(), NOW()),
(uuid_generate_v4(), 'Ciência da Computação', 'da3d46d7-f131-4bbd-84bd-043df2b7ce35', NOW(), NOW()),
(uuid_generate_v4(), 'Enfermagem', '47aa992f-4f8e-4b21-a412-e3a221cc99bb', NOW(), NOW()),
(uuid_generate_v4(), 'Psicologia', '31d05c2b-d083-4c92-9464-0ca9a83e23c9', NOW(), NOW()),
(uuid_generate_v4(), 'Educação Física', 'cf0f022e-3ae2-4539-8ab2-c9c2ae07a030', NOW(), NOW()),
(uuid_generate_v4(), 'Administração', '3f9e86e1-8c35-4b67-8480-0eb5cd9774ce', NOW(), NOW()),
(uuid_generate_v4(), 'Ciências Contábeis', '53cfaab9-bdda-4619-b0cd-5a0e767e7fd6', NOW(), NOW()),
(uuid_generate_v4(), 'Matemática', '9b65bba9-7288-448c-bf1d-16981767b72c', NOW(), NOW()),
(uuid_generate_v4(), 'Física', '970919bc-d8c5-4f47-abad-d9fc0cd8c4a4', NOW(), NOW()),
(uuid_generate_v4(), 'Química', '6cb3c1cc-16e1-4cce-b590-b4c8c286a80c', NOW(), NOW()),
(uuid_generate_v4(), 'Letras - Português', 'f60ce2b7-e29c-45db-8d9d-a8c65a3ce684', NOW(), NOW()),
(uuid_generate_v4(), 'Letras - Inglês', 'f60ce2b7-e29c-45db-8d9d-a8c65a3ce684', NOW(), NOW()),
(uuid_generate_v4(), 'Biologia', '4f55d3b5-9a70-4a64-8952-4d3c45b54fbf', NOW(), NOW()),
(uuid_generate_v4(), 'Ciências Sociais', '3cb026bb-9104-4279-a2b6-2940260f3fdd', NOW(), NOW());