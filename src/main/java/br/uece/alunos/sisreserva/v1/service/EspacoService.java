package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;

public interface EspacoService {
    EspacoRetornoDTO criarEspaco(EspacoDTO data);
}
