package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.CriarEspaco;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EspacoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EspacoServiceImpl implements EspacoService {
    private final CriarEspaco criarEspaco;

    @Override
    public EspacoRetornoDTO criarEspaco(EspacoDTO data) {
        return criarEspaco.criarEspaco(data);
    }
}
