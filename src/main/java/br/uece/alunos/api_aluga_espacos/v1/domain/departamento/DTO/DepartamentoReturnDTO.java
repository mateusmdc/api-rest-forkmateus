package br.uece.alunos.api_aluga_espacos.v1.domain.departamento.DTO;

import br.uece.alunos.api_aluga_espacos.v1.domain.departamento.Departamento;

public record DepartamentoReturnDTO(String id, String nome) {
    public DepartamentoReturnDTO(Departamento departamento) {
        this(departamento.getId(), departamento.getNome());
    }
}
