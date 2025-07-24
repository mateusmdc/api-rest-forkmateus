package br.uece.alunos.sisreserva.v1.dto.projeto;

import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;

import java.time.LocalDate;


public record ProjetoRetornoDTO(
    String id,
    String nome,
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim,
    String usuarioResponsavelId,
    String instituicaoId
) {
    public ProjetoRetornoDTO(Projeto projeto) {
        this(
            projeto.getId(),
            projeto.getNome(),
            projeto.getDescricao(),
            projeto.getDataInicio(),
            projeto.getDataFim(),
            projeto.getUsuarioResponsavel().getId(),
            projeto.getInstituicao().getId()
        );
    }
}

    

