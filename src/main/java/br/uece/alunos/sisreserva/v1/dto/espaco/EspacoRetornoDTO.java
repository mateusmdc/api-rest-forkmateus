package br.uece.alunos.sisreserva.v1.dto.espaco;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.localizacao.LocalizacaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoAtividade.TipoAtividadeRetornoDTO;

public record EspacoRetornoDTO(
        String id,
        String nome,
        String urlCnpq,
        String observacao,
        DepartamentoRetornoDTO departamento,
        LocalizacaoRetornoDTO localizacao,
        TipoEspacoRetornoDTO tipoEspaco,
        TipoAtividadeRetornoDTO tipoAtividade,
        Boolean precisaProjeto
) {
    public EspacoRetornoDTO(Espaco espaco) {
        this(
                espaco.getId(),
                espaco.getNome(),
                espaco.getUrlCnpq(),
                espaco.getObservacao(),
                new DepartamentoRetornoDTO(espaco.getDepartamento()),
                new LocalizacaoRetornoDTO(espaco.getLocalizacao()),
                new TipoEspacoRetornoDTO(espaco.getTipoEspaco()),
                new TipoAtividadeRetornoDTO(espaco.getTipoAtividade()),
                espaco.getPrecisaProjeto()
        );
    }
}
