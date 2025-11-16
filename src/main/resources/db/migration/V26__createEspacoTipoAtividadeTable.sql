-- Migration para criar tabela de relacionamento ManyToMany entre Espaco e TipoAtividade
-- Esta migration faz parte da mudança de regra de negócio onde um espaço pode ter múltiplos tipos de atividade

-- Criar tabela de relacionamento
CREATE TABLE espaco_tipo_atividade (
    espaco_id VARCHAR(36) NOT NULL,
    tipo_atividade_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (espaco_id, tipo_atividade_id),
    CONSTRAINT fk_espaco_tipo_atividade_espaco 
        FOREIGN KEY (espaco_id) REFERENCES espaco(id) ON DELETE CASCADE,
    CONSTRAINT fk_espaco_tipo_atividade_tipo_atividade 
        FOREIGN KEY (tipo_atividade_id) REFERENCES tipo_atividade(id) ON DELETE CASCADE
);

-- Criar índices para melhorar performance nas consultas
CREATE INDEX idx_espaco_tipo_atividade_espaco_id ON espaco_tipo_atividade(espaco_id);
CREATE INDEX idx_espaco_tipo_atividade_tipo_atividade_id ON espaco_tipo_atividade(tipo_atividade_id);
