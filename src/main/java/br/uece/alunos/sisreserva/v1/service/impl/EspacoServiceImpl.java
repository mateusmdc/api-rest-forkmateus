package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.AtualizarEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.CriarEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ObterEspaco;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EspacoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EspacoServiceImpl implements EspacoService {
    private final AtualizarEspaco atualizarEspaco;
    private final CriarEspaco criarEspaco;
    private final ObterEspaco obterEspaco;

    @Override
    public EspacoRetornoDTO atualizar(String id, EspacoAtualizarDTO data) {
        return atualizarEspaco.atualizar(id, data);
    }

    @Override
    public EspacoRetornoDTO criarEspaco(EspacoDTO data) {
        return criarEspaco.criarEspaco(data);
    }

    @Override
    public Page<EspacoRetornoDTO> obterEspacos(Pageable pageable, String id, String departamento, String localizacao, String tipoEspaco, String tipoAtividade) {
        return obterEspaco.obterEspacos(pageable, id, departamento, localizacao, tipoEspaco, tipoAtividade);
    }
}
