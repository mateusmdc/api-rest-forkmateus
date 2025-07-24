package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.comite.ComiteAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteDTO;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComiteService {
    ComiteRetornoDTO atualizar(ComiteAtualizarDTO data, String comiteId);
    ComiteRetornoDTO criar(ComiteDTO data);
    Page<ComiteRetornoDTO> obter(Pageable pageable, String id, Integer tipoCodigo);
}
