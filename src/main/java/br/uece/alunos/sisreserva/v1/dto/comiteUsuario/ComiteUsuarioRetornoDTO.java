package br.uece.alunos.sisreserva.v1.dto.comiteUsuario;

import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.ComiteUsuario;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;

import java.time.LocalDateTime;

public record ComiteUsuarioRetornoDTO(
        String id,
        ComiteRetornoDTO comite,
        UsuarioRetornoDTO usuario,
        DepartamentoRetornoDTO departamento,
        String descricao,
        String portaria,
        Boolean isTitular,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public ComiteUsuarioRetornoDTO(ComiteUsuario c) {
        this(
                c.getId(),
                new ComiteRetornoDTO(c.getComite()),
                new UsuarioRetornoDTO(c.getUsuario()),
                c.getDepartamento() != null ? new DepartamentoRetornoDTO(c.getDepartamento()) : null,
                c.getDescricao(),
                c.getPortaria(),
                c.getIsTitular(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
