package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComiteUsuarioService {
    Page<ComiteUsuarioRetornoDTO> obter(
            Pageable pageable,
            String id,
            String comiteId,
            String usuarioId,
            String departamentoId,
            String portaria,
            Boolean isTitular
    );
}
