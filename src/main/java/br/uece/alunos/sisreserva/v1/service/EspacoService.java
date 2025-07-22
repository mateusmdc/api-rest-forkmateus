package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EspacoService {
    EspacoRetornoDTO atualizar(String id, EspacoAtualizarDTO data);
    EspacoRetornoDTO criarEspaco(EspacoDTO data);
    Page<EspacoRetornoDTO> obterEspacos(Pageable pageable,
                                        String id,
                                        String departamento,
                                        String localizacao,
                                        String tipoEspaco,
                                        String tipoAtividade);
}
