package br.uece.alunos.sisreserva.v1.dto.curso;

import br.uece.alunos.sisreserva.v1.domain.curso.Curso;
import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;

public record CursoReturnDTO(String id, String nome, DepartamentoRetornoDTO departamento) {
    public CursoReturnDTO(Curso curso) {
        this(curso.getId(), curso.getNome(), new DepartamentoRetornoDTO(curso.getDepartamento()));
    }
}
