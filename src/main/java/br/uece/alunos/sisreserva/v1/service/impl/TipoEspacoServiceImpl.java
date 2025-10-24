package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase.AtualizarTipoEspaco;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase.CriarTipoEspaco;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase.DeletarTipoEspaco;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase.ObterTiposEspaco;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.TipoEspacoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoEspacoServiceImpl implements TipoEspacoService {
    private final CriarTipoEspaco criarTipoEspaco;
    private final AtualizarTipoEspaco atualizarTipoEspaco;
    private final DeletarTipoEspaco deletarTipoEspaco;
    private final ObterTiposEspaco obterTiposEspaco;

    @Override
    public TipoEspacoRetornoDTO criar(TipoEspacoDTO data) {
        return criarTipoEspaco.criar(data);
    }

    @Override
    public TipoEspacoRetornoDTO atualizar(String id, TipoEspacoAtualizarDTO data) {
        return atualizarTipoEspaco.atualizar(id, data);
    }

    @Override
    public void deletar(String id) {
        deletarTipoEspaco.deletar(id);
    }

    @Override
    public Page<TipoEspacoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterTiposEspaco.obter(pageable, id, nome);
    }
}
