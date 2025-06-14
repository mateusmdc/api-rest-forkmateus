package br.uece.alunos.sisreserva.v1.domain.departamento.DTO;

import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;

public record DepartamentoReturnDTO(String id, String nome) {
    public DepartamentoReturnDTO(Departamento departamento) {
        this(departamento.getId(), departamento.getNome());
    }
}
