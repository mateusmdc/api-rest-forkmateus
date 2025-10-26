package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.AtribuirEspacosAoComplexo;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.AtualizarComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.CriarComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.DeletarComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.DesatribuirEspacosDoComplexo;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.ListarEspacosDoComplexo;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase.ObterComplexoEspacos;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.ComplexoEspacosService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ComplexoEspacosServiceImpl implements ComplexoEspacosService {
    private final CriarComplexoEspacos criarComplexoEspacos;
    private final AtualizarComplexoEspacos atualizarComplexoEspacos;
    private final DeletarComplexoEspacos deletarComplexoEspacos;
    private final ObterComplexoEspacos obterComplexoEspacos;
    private final AtribuirEspacosAoComplexo atribuirEspacosAoComplexo;
    private final DesatribuirEspacosDoComplexo desatribuirEspacosDoComplexo;
    private final ListarEspacosDoComplexo listarEspacosDoComplexo;

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

    @Override
    public ComplexoEspacosRetornoDTO atribuirEspacos(String id, List<String> espacoIds) {
        return atribuirEspacosAoComplexo.atribuir(id, espacoIds);
    }

    @Override
    public ComplexoEspacosRetornoDTO desatribuirEspacos(String id, List<String> espacoIds) {
        return desatribuirEspacosDoComplexo.desatribuir(id, espacoIds);
    }

    @Override
    public List<EspacoRetornoDTO> listarEspacos(String id) {
        return listarEspacosDoComplexo.listar(id);
    }
}
