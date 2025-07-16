package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.localizacao.LocalizacaoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocalizacaoService {
    Page<LocalizacaoRetornoDTO> obter(Pageable pageable, String id, String nome);
}
