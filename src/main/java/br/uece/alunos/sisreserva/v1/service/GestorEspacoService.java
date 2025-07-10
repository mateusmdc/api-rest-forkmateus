package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;

public interface GestorEspacoService {
    GestorEspacoRetornoDTO cadastrarGestorEspaco(GestorEspacoDTO data);
}
