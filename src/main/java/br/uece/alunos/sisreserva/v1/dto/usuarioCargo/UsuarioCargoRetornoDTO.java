package br.uece.alunos.sisreserva.v1.dto.usuarioCargo;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargo;
import br.uece.alunos.sisreserva.v1.dto.cargo.CargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;

public record UsuarioCargoRetornoDTO(
        CargoRetornoDTO cargo,
        UsuarioRetornoDTO usuario
) {
    public UsuarioCargoRetornoDTO(UsuarioCargo data) {
        this(
                new CargoRetornoDTO(data.getCargo()),
                new UsuarioRetornoDTO(data.getUsuario())
        );
    }
}