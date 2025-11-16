package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoEspacoService {
    TipoEspacoRetornoDTO criar(TipoEspacoDTO data);
    TipoEspacoRetornoDTO atualizar(String id, TipoEspacoAtualizarDTO data);
    void deletar(String id);
    Page<TipoEspacoRetornoDTO> obter(Pageable pageable, String id, String nome);
}
