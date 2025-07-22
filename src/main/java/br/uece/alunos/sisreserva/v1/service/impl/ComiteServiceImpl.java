package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.comite.useCase.AtualizarComite;
import br.uece.alunos.sisreserva.v1.domain.comite.useCase.CriarComite;
import br.uece.alunos.sisreserva.v1.domain.comite.useCase.ObterComites;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteDTO;
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
    private final AtualizarComite atualizarComite;
    private final CriarComite criarComite;
    private final ObterComites obterComites;

    @Override
    public ComiteRetornoDTO atualizar(ComiteAtualizarDTO data, String comiteId) {
        return atualizarComite.atualizar(data, comiteId);
    }

    @Override
    public ComiteRetornoDTO criar(ComiteDTO data) {
        return criarComite.criar(data);
    }

    @Override
    public Page<ComiteRetornoDTO> obter(Pageable pageable, String id, Integer tipoCodigo) {
        return obterComites.obter(pageable, id, tipoCodigo);
    }
}
