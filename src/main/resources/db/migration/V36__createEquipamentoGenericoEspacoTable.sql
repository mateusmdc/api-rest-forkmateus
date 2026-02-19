-- Tabela de relacionamento entre Equipamento Genérico e Espaço
-- Registra as quantidades de equipamentos genéricos alocados em cada espaço
-- Exemplo: Espaço "Laboratório A" possui 30 Cadeiras

CREATE TABLE equipamento_generico_espaco (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    equipamento_generico_id VARCHAR(36) NOT NULL,
    espaco_id VARCHAR(36) NOT NULL,
    quantidade INTEGER NOT NULL CHECK (quantidade >= 1),
    data_vinculo TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP NOT NULL,
    
    -- Chaves estrangeiras com cascade delete
    CONSTRAINT fk_equipamento_generico_espaco_equipamento_generico 
        FOREIGN KEY (equipamento_generico_id) 
        REFERENCES equipamento_generico(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT fk_equipamento_generico_espaco_espaco 
        FOREIGN KEY (espaco_id) 
        REFERENCES espaco(id) 
        ON DELETE CASCADE,
    
    -- Constraint único: um equipamento genérico só pode ser vinculado uma vez ao mesmo espaço
    CONSTRAINT uk_equipamento_generico_espaco 
        UNIQUE (equipamento_generico_id, espaco_id)
);

-- Índices para melhorar performance das consultas
CREATE INDEX idx_equipamento_generico_espaco_equipamento 
    ON equipamento_generico_espaco(equipamento_generico_id);

CREATE INDEX idx_equipamento_generico_espaco_espaco 
    ON equipamento_generico_espaco(espaco_id);

-- Comentários da tabela
COMMENT ON TABLE equipamento_generico_espaco IS 
    'Relacionamento entre equipamentos genéricos e espaços com quantidade';

COMMENT ON COLUMN equipamento_generico_espaco.id IS 
    'Identificador único do vínculo';

COMMENT ON COLUMN equipamento_generico_espaco.equipamento_generico_id IS 
    'Referência ao equipamento genérico vinculado';

COMMENT ON COLUMN equipamento_generico_espaco.espaco_id IS 
    'Referência ao espaço onde o equipamento está alocado';

COMMENT ON COLUMN equipamento_generico_espaco.quantidade IS 
    'Quantidade do equipamento genérico presente no espaço';

COMMENT ON COLUMN equipamento_generico_espaco.data_vinculo IS 
    'Data e hora em que o equipamento foi vinculado ao espaço';

COMMENT ON COLUMN equipamento_generico_espaco.data_atualizacao IS 
    'Data e hora da última atualização da quantidade';
