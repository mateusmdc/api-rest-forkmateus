-- Popula a tabela de relacionamento espaco_tipo_atividade com dados de exemplo
-- Este script é reexecutado a cada migração (prefixo R__) para manter os dados de teste

-- Limpa dados existentes para evitar duplicatas
DELETE FROM espaco_tipo_atividade;

-- Associa o LABCOMP ao tipo de atividade 'Ensino'
-- Espaço: LABCOMP (cc20b6e6-dc56-4db6-92fa-df47c99961bb)
-- TipoAtividade: Ensino (1a4c289e-5b97-4c63-bf36-c3b3d8149250)
INSERT INTO espaco_tipo_atividade (espaco_id, tipo_atividade_id) VALUES 
('cc20b6e6-dc56-4db6-92fa-df47c99961bb', '1a4c289e-5b97-4c63-bf36-c3b3d8149250');
