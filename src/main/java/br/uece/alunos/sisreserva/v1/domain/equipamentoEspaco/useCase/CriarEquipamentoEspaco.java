package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.CriarEquipamentoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarEquipamentoEspaco {
    @Autowired
    private EquipamentoEspacoRepository repository;

    public EquipamentoEspacoRetornoDTO criarEquipamentoAlocandoAoEspaco(CriarEquipamentoEspacoDTO data) {

    }
}
