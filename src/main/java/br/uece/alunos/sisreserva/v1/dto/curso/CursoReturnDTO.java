package br.uece.alunos.sisreserva.v1.dto.curso;

import br.uece.alunos.sisreserva.v1.domain.curso.Curso;

public record CursoReturnDTO(String id, String nome, String departamentoId, String departamentoNome) {
    public CursoReturnDTO(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getDepartamento().getId(), curso.getDepartamento().getNome());
    }
}
