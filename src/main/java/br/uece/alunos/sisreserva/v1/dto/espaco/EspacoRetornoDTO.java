package br.uece.alunos.sisreserva.v1.dto.espaco;

public record EspacoRetornoDTO(String id,
                               String nome,
                               String urlCnpq,
                               String observacao,
                               String departamentoId,
                               String localizacaoId,
                               String tipoEspacoId,
                               String tipoAtividadeId,
                               Boolean precisaProjeto) {
}
