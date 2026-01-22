package br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacos;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;

/**
 * DTO de retorno para gestor de complexo de espaços.
 * Contém informações completas do gestor, complexo e status.
 */
public record GestorComplexoEspacosRetornoDTO(
        String id,
        ComplexoEspacosRetornoDTO complexoEspacos,
        UsuarioRetornoDTO gestor,
        Boolean estaAtivo
) {
    public GestorComplexoEspacosRetornoDTO(GestorComplexoEspacos gestorComplexoEspacos) {
        this(
                gestorComplexoEspacos.getId(),
                new ComplexoEspacosRetornoDTO(gestorComplexoEspacos.getComplexoEspacos()),
                new UsuarioRetornoDTO(gestorComplexoEspacos.getUsuarioGestor()),
                gestorComplexoEspacos.getEstaAtivo()
        );
    }
}
