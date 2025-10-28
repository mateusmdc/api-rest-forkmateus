package br.uece.alunos.sisreserva.v1.dto.espaco;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.localizacao.LocalizacaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoAtividade.TipoAtividadeRetornoDTO;

import java.util.List;

/**
 * DTO para retorno de informações de espaço.
 * 
 * @param id identificador único do espaço
 * @param nome nome do espaço
 * @param urlCnpq URL do CNPq
 * @param observacao observações sobre o espaço
 * @param departamento dados do departamento
 * @param localizacao dados da localização
 * @param tipoEspaco dados do tipo de espaço
 * @param tiposAtividade lista de tipos de atividade do espaço
 * @param precisaProjeto indica se o espaço precisa de projeto
 * @param multiusuario indica se o espaço é multiusuário
 */
public record EspacoRetornoDTO(
        String id,
        String nome,
        String urlCnpq,
        String observacao,
        DepartamentoRetornoDTO departamento,
        LocalizacaoRetornoDTO localizacao,
        TipoEspacoRetornoDTO tipoEspaco,
        List<TipoAtividadeRetornoDTO> tiposAtividade,
        Boolean precisaProjeto,
        Boolean multiusuario
) {
    /**
     * Construtor que converte uma entidade Espaco para o DTO de retorno.
     * 
     * @param espaco entidade Espaco a ser convertida
     */
    public EspacoRetornoDTO(Espaco espaco) {
        this(
                espaco.getId(),
                espaco.getNome(),
                espaco.getUrlCnpq(),
                espaco.getObservacao(),
                new DepartamentoRetornoDTO(espaco.getDepartamento()),
                new LocalizacaoRetornoDTO(espaco.getLocalizacao()),
                new TipoEspacoRetornoDTO(espaco.getTipoEspaco()),
                espaco.getTiposAtividade() != null 
                    ? espaco.getTiposAtividade().stream()
                        .map(TipoAtividadeRetornoDTO::new)
                        .toList()
                    : List.of(),
                espaco.getPrecisaProjeto(),
                espaco.getMultiusuario()
        );
    }
}
