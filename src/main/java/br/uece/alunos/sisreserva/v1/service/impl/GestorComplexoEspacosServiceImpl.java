package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase.CadastraOuReativaGestorComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase.InativarGestorComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase.ObterGestorComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase.ValidadorGestorComplexoEspacos;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.GestorComplexoEspacosService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço de gestores de complexos de espaços.
 * Delega operações para os use cases correspondentes.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class GestorComplexoEspacosServiceImpl implements GestorComplexoEspacosService {
    private final CadastraOuReativaGestorComplexoEspacos cadastraOuReativaGestorComplexoEspacos;
    private final InativarGestorComplexoEspacos inativarGestorComplexoEspacos;
    private final ObterGestorComplexoEspacos obterGestorComplexoEspacos;
    private final ValidadorGestorComplexoEspacos validadorGestorComplexoEspacos;

    @Override
    public GestorComplexoEspacosRetornoDTO cadastrarOuReativarGestorComplexoEspacos(GestorComplexoEspacosDTO data) {
        return cadastraOuReativaGestorComplexoEspacos.executar(data);
    }

    @Override
    public GestorComplexoEspacosRetornoDTO inativar(String gestorComplexoEspacosId) {
        return inativarGestorComplexoEspacos.inativar(gestorComplexoEspacosId);
    }

    @Override
    public Page<GestorComplexoEspacosRetornoDTO> obter(Pageable pageable, String id, String complexoEspacosId, String gestorId, boolean todos) {
        return obterGestorComplexoEspacos.obter(pageable, id, complexoEspacosId, gestorId, todos);
    }

    @Override
    public void validarGestorAtivo(String usuarioId, String complexoEspacosId) {
        validadorGestorComplexoEspacos.validarGestorAtivo(usuarioId, complexoEspacosId);
    }
}
