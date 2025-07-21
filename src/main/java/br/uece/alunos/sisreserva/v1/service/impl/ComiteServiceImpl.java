package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.comite.useCase.ObterComites;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.ComiteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ComiteServiceImpl implements ComiteService {
    private final ObterComites obterComites;

    @Override
    public Page<ComiteRetornoDTO> obter(Pageable pageable, String id, Integer tipoCodigo) {
        return obterComites.obter(pageable, id, tipoCodigo);
    }
}
