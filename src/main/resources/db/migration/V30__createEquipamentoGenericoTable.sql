-- Tabela para armazenar equipamentos genéricos (cadeiras, lixeiras, lousas, etc)
-- Equipamentos que são utilizados em grande quantidade em espaços

CREATE TABLE equipamento_generico (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índice para busca por nome
CREATE INDEX idx_equipamento_generico_nome ON equipamento_generico(nome);

-- Comentários na tabela e colunas
COMMENT ON TABLE equipamento_generico IS 'Tabela de equipamentos genéricos utilizados em espaços (cadeiras, lixeiras, lousas, etc)';
COMMENT ON COLUMN equipamento_generico.id IS 'Identificador único do equipamento genérico';
COMMENT ON COLUMN equipamento_generico.nome IS 'Nome do equipamento genérico';
COMMENT ON COLUMN equipamento_generico.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN equipamento_generico.updated_at IS 'Data e hora da última atualização do registro';
