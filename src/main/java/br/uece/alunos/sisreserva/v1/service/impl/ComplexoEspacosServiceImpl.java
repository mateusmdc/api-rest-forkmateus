package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.AtualizarComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.CriarComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.DeletarComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.ObterComplexoEspacos;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.ComplexoEspacosService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ComplexoEspacosServiceImpl implements ComplexoEspacosService {
    private final CriarComplexoEspacos criarComplexoEspacos;
    private final AtualizarComplexoEspacos atualizarComplexoEspacos;
    private final DeletarComplexoEspacos deletarComplexoEspacos;
    private final ObterComplexoEspacos obterComplexoEspacos;

    @Override
    public ComplexoEspacosRetornoDTO criar(ComplexoEspacosDTO data) {
        return criarComplexoEspacos.criar(data);
    }

    @Override
    public ComplexoEspacosRetornoDTO atualizar(String id, ComplexoEspacosAtualizarDTO data) {
        return atualizarComplexoEspacos.atualizar(id, data);
    }

    @Override
    public void deletar(String id) {
        deletarComplexoEspacos.deletar(id);
    }

    @Override
    public Page<ComplexoEspacosRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterComplexoEspacos.obter(pageable, id, nome);
    }
}
