package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GestorEspacoService {
    GestorEspacoRetornoDTO cadastrarOuReativarGestorEspaco(GestorEspacoDTO data);
    GestorEspacoRetornoDTO inativar(String gestorEspacoId);
    Page<GestorEspacoRetornoDTO> obter(Pageable pageable, String id, String espacoId, String gestorId, boolean todos);
    void validarGestorAtivo(String usuarioId, String espacoId);
}
