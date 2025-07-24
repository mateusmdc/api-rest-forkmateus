package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoEspacoService {
    Page<TipoEspacoRetornoDTO> obter(Pageable pageable, String id, String nome);
}
