package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.CadastraOuReativaGestorEspaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.CadastrarGestorEspaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.ObterGestorEspaco;
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
    private final ObterGestorEspaco obterGestorEspaco;

    @Override
    public GestorEspacoRetornoDTO cadastrarOuReativarGestorEspaco(GestorEspacoDTO data) {
        return cadastraOuReativaGestorEspaco.executar(data);
    }

    @Override
    public Page<GestorEspacoRetornoDTO> obter(Pageable pageable, String id, String espacoId, String gestorId) {
        return obterGestorEspaco.obter(pageable, id, espacoId, gestorId);
    }
}
