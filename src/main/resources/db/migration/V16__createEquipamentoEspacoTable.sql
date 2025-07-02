CREATE TABLE equipamento_espaco (
    id VARCHAR(36) PRIMARY KEY,
    equipamento_id VARCHAR(36) REFERENCES equipamento(id) ON DELETE CASCADE,
    espaco_id VARCHAR(36) REFERENCES espaco(id) ON DELETE CASCADE,
    data_alocacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_remocao TIMESTAMP
);