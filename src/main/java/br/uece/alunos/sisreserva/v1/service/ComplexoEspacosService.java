package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComplexoEspacosService {
    ComplexoEspacosRetornoDTO criar(ComplexoEspacosDTO data);
    ComplexoEspacosRetornoDTO atualizar(String id, ComplexoEspacosAtualizarDTO data);
    void deletar(String id);
    Page<ComplexoEspacosRetornoDTO> obter(Pageable pageable, String id, String nome);
}
