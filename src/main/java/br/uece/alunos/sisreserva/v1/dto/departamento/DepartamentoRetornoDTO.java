package br.uece.alunos.sisreserva.v1.dto.departamento;

import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;

public record DepartamentoRetornoDTO(String id, String nome) {
    public DepartamentoRetornoDTO(Departamento departamento) {
        this(departamento.getId(), departamento.getNome());
    }
}
