package br.uece.alunos.sisreserva.v1.dto.gestorEspaco;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspaco;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;

public record GestorEspacoRetornoDTO(
        String id,
        EspacoRetornoDTO espaco,
        UsuarioRetornoDTO gestor,
        Boolean estaAtivo
) {
    public GestorEspacoRetornoDTO(GestorEspaco gestorEspaco) {
        this(
                gestorEspaco.getId(),
                new EspacoRetornoDTO(gestorEspaco.getEspaco()),
                new UsuarioRetornoDTO(gestorEspaco.getUsuarioGestor()),
                gestorEspaco.getEstaAtivo()
        );
    }
}
