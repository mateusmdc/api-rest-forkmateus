package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase.CriarEquipamentoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase.ObterEquipamentosEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.CriarEquipamentoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoEspacoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EquipamentoEspacoServiceImpl implements EquipamentoEspacoService {
    private final CriarEquipamentoEspaco criarEquipamentoEspaco;
    private final ObterEquipamentosEspaco obterEquipamentosEspaco;

    @Override
    public EquipamentoEspacoRetornoDTO criarEquipamentoAlocandoAoEspaco(CriarEquipamentoEspacoDTO data) {
        return criarEquipamentoEspaco.criarEquipamentoAlocandoAoEspaco(data);
    }

    @Override
    public Page<EquipamentoEspacoRetornoDTO> obter(Pageable pageable, String id, String equipamentoId, String tipoEquipamentoId, String espacoId, LocalDateTime dataInicio, LocalDateTime dataFim, String tipoEquipamentoNome, String espacoNome) {
        return obterEquipamentosEspaco.obter(pageable, id, equipamentoId, tipoEquipamentoId, espacoId, dataInicio, dataFim, tipoEquipamentoNome, espacoNome);
    }
}
