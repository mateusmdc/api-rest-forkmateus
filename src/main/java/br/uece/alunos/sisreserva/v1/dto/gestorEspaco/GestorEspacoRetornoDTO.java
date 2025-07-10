package br.uece.alunos.sisreserva.v1.dto.gestorEspaco;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspaco;

public record GestorEspacoRetornoDTO(
        String id,
        String espacoId,
        String espacoNome,
        String gestorId,
        String gestorNome,
        Boolean estaAtivo
) {
    public GestorEspacoRetornoDTO(GestorEspaco gestorEspaco) {
        this(
                gestorEspaco.getId(),
                gestorEspaco.getEspaco().getId(),
                gestorEspaco.getEspaco().getNome(),
                gestorEspaco.getUsuarioGestor().getId(),
                gestorEspaco.getUsuarioGestor().getNome(),
                gestorEspaco.getEstaAtivo()
        );
    }
}
