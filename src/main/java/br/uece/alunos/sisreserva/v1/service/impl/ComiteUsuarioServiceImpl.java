package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.useCase.CriarComiteUsuario;
import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.useCase.DeletarComiteUsuario;
import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.useCase.ObterComiteUsuarios;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.ComiteUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ComiteUsuarioServiceImpl implements ComiteUsuarioService {
    private final CriarComiteUsuario criarComiteUsuario;
    private final DeletarComiteUsuario deletarComiteUsuario;
    private final ObterComiteUsuarios obterComiteUsuarios;

    @Override
    public ComiteUsuarioRetornoDTO criar(ComiteUsuarioDTO data) {
        return criarComiteUsuario.criar(data);
    }

    @Override
    public void deletar(String id) {
        deletarComiteUsuario.deletar(id);
    }

    @Override
    public Page<ComiteUsuarioRetornoDTO> obter(Pageable pageable, String id, String comiteId, String usuarioId, String departamentoId, String portaria, Boolean isTitular) {
        return obterComiteUsuarios.obter(pageable, id, comiteId, usuarioId, departamentoId, portaria, isTitular);
    }
}
