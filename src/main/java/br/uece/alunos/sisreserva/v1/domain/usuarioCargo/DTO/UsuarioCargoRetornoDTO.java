package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.DTO;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargo;

public record UsuarioCargoRetornoDTO(String cargoId, String cargoNome, String UsuarioId) {
    public UsuarioCargoRetornoDTO(UsuarioCargo data) {
        this(data.getCargo().getId(), data.getCargo().getNome(), data.getUsuario().getId());
    }
}
