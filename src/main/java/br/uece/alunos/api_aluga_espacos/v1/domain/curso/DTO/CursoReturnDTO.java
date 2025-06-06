package br.uece.alunos.api_aluga_espacos.v1.domain.curso.DTO;

import br.uece.alunos.api_aluga_espacos.v1.domain.curso.Curso;

public record CursoReturnDTO(String id, String nome, String departamentoId, String departamentoNome) {
    public CursoReturnDTO(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getDepartamento().getId(), curso.getDepartamento().getNome());
    }
}
