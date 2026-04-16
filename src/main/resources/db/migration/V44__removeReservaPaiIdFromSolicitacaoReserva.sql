-- Remove o campo legado reserva_pai_id e seu índice da tabela solicitacao_reserva.
--
-- Este campo pertencia ao modelo antigo de recorrência, onde cada ocorrência era
-- armazenada como um registro separado e apontava para a "reserva mãe" via reserva_pai_id.
-- O novo modelo utiliza isSerie = true com ocorrências calculadas dinamicamente
-- e exceções registradas em excecao_recorrencia, tornando este campo obsoleto.
-- A entidade SolicitacaoReserva não mapeia mais reserva_pai_id desde o PR de migração.

DROP INDEX IF EXISTS idx_solicitacao_reserva_pai;

ALTER TABLE solicitacao_reserva
    DROP COLUMN IF EXISTS reserva_pai_id;
