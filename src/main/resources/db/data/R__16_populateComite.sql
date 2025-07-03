-- Comitê Gestor (tipo 1 - único para todos os multiusuários)
INSERT INTO comite (id, descricao, tipo)
VALUES ('5e6fcbf4-b8d2-4c27-8d19-56efc7bcb479', 'Comitê Gestor Geral', 1);

-- Comitês de Usuários (tipo 2 - por áreas)
INSERT INTO comite (id, descricao, tipo)
VALUES 
  ('c8d4b6e2-6a61-4cf2-b3b5-a4690c589482', 'Comitê de Usuários - Biologia', 2),
  ('a3f15df3-6ecf-4b1e-bd3f-9f5f95b2e470', 'Comitê de Usuários - Física', 2),
  ('25761ec1-d328-4a25-b737-985994563a2e', 'Comitê de Usuários - Ciências da Computação', 2);

-- Técnicos Associados (tipo 3)
INSERT INTO comite (id, descricao, tipo)
VALUES ('af7b2176-4e1e-46bc-9e91-cb8245a70157', 'Comitê de Técnicos Associados', 3);

-- Representante Discente da Pós-Graduação (tipo 4)
INSERT INTO comite (id, descricao, tipo)
VALUES ('d2a2f97b-2b1b-4451-a218-c61d9ae72e70', 'Representante Discente da Pós-Graduação', 4);
