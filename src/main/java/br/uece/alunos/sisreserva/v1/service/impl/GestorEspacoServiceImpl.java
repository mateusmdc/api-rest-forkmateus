package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.CadastraOuReativaGestorEspaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.InativarGestorEspaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.ObterGestorEspaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.ValidadorGestorEspaco;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.GestorEspacoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GestorEspacoServiceImpl implements GestorEspacoService {
    private final CadastraOuReativaGestorEspaco cadastraOuReativaGestorEspaco;
    private final InativarGestorEspaco inativarGestorEspaco;
    private final ObterGestorEspaco obterGestorEspaco;
    private final ValidadorGestorEspaco validadorGestorEspaco;

    @Override
    public GestorEspacoRetornoDTO cadastrarOuReativarGestorEspaco(GestorEspacoDTO data) {
        return cadastraOuReativaGestorEspaco.executar(data);
    }

    @Override
    public GestorEspacoRetornoDTO inativar(String gestorEspacoId) {
        return inativarGestorEspaco.inativar(gestorEspacoId);
    }

    @Override
    public Page<GestorEspacoRetornoDTO> obter(Pageable pageable, String id, String espacoId, String gestorId, boolean todos) {
        return obterGestorEspaco.obter(pageable, id, espacoId, gestorId, todos);
    }

    @Override
    public void validarGestorAtivo(String usuarioId, String espacoId) {
        validadorGestorEspaco.validarGestorAtivo(usuarioId, espacoId);
    }
}
