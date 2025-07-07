package br.uece.alunos.sisreserva.v1.dto.espaco;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;

public record EspacoRetornoDTO(String id,
                               String nome,
                               String urlCnpq,
                               String observacao,
                               String departamentoId,
                               String localizacaoId,
                               String tipoEspacoId,
                               String tipoAtividadeId,
                               Boolean precisaProjeto) {
    public EspacoRetornoDTO(Espaco espaco) {
        this(espaco.getId(), espaco.getNome(), espaco.getUrlCnpq(), espaco.getObservacao(), espaco.getDepartamento().getId(),
                espaco.getLocalizacao().getId(), espaco.getTipoEspaco().getId(), espaco.getTipoAtividade().getId(), espaco.getPrecisaProjeto());
    }
}
