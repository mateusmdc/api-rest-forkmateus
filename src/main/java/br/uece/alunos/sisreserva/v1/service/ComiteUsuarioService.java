package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComiteUsuarioService {
    ComiteUsuarioRetornoDTO atualizar(String id, ComiteUsuarioAtualizarDTO data);
    ComiteUsuarioRetornoDTO criar(ComiteUsuarioDTO data);
    void deletar(String id);
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
