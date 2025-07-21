package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.comite.ComiteRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComiteService {
    Page<ComiteRetornoDTO> obter(Pageable pageable, String id, Integer tipoCodigo);
}
