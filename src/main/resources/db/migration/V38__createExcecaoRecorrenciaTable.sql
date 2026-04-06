-- Cria a tabela excecao_recorrencia para armazenar exceções pontuais
-- de ocorrências em séries de reservas recorrentes (novo modelo).
--
-- Quando um gestor ou administrador precisar cancelar, reagendar ou alterar
-- o status de uma única ocorrência de uma série, um registro é criado aqui,
-- em vez de alterar toda a série ou criar múltiplos registros.
--
-- Restrição uq_excecao_reserva_data garante no máximo uma exceção por data em cada série.
CREATE TABLE excecao_recorrencia
(
    id                      VARCHAR(36)   NOT NULL,
    solicitacao_reserva_id  VARCHAR(36)   NOT NULL,
    data_ocorrencia         DATE          NOT NULL,
    data_inicio_nova        TIMESTAMP      NULL,
    data_fim_nova           TIMESTAMP      NULL,
    status                  INT           NOT NULL,
    motivo                  VARCHAR(500)  NULL,
    created_at              TIMESTAMP      NULL,
    updated_at              TIMESTAMP      NULL,
    CONSTRAINT pk_excecao_recorrencia PRIMARY KEY (id),
    CONSTRAINT fk_excecao_solicitacao FOREIGN KEY (solicitacao_reserva_id)
        REFERENCES solicitacao_reserva (id) ON DELETE CASCADE,
    CONSTRAINT uq_excecao_reserva_data UNIQUE (solicitacao_reserva_id, data_ocorrencia)
);
