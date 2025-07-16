package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.instituicao.InstituicaoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstituicaoService {
    Page<InstituicaoRetornoDTO> obter(Pageable pageable, String id, String nome);

}
