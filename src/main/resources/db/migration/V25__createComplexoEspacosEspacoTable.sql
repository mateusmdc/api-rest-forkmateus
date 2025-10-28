CREATE TABLE complexo_espacos_espaco (
    complexo_espacos_id VARCHAR(36) NOT NULL,
    espaco_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (complexo_espacos_id, espaco_id),
    CONSTRAINT fk_complexo_espacos 
        FOREIGN KEY (complexo_espacos_id) 
        REFERENCES complexo_espacos(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_espaco 
        FOREIGN KEY (espaco_id) 
        REFERENCES espaco(id) 
        ON DELETE CASCADE
);
