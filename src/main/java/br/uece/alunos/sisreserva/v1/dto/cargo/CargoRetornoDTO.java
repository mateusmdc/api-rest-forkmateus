package br.uece.alunos.sisreserva.v1.dto.cargo;

import br.uece.alunos.sisreserva.v1.domain.cargo.Cargo;

public record CargoRetornoDTO(String id, String nome, String descricao) {
    public CargoRetornoDTO(Cargo cargo) {
        this(cargo.getId(), cargo.getNome(), cargo.getDescricao());
    }
}