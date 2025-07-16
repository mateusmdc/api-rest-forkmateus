package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartamentoService {
    Page<DepartamentoRetornoDTO> obter(Pageable pageable, String id, String nome);
}
