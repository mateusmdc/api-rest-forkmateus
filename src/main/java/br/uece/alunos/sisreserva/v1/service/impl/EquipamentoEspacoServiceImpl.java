package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase.CriarEquipamentoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase.InativarEquipamentoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase.ObterEquipamentosEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.CriarEquipamentoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoEspacoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoEspacoServiceImpl implements EquipamentoEspacoService {
    private final CriarEquipamentoEspaco criarEquipamentoEspaco;
    private final InativarEquipamentoEspaco inativarEquipamentoEspaco;
    private final ObterEquipamentosEspaco obterEquipamentosEspaco;

    @Override
    public List<EquipamentoEspacoRetornoDTO> criarEquipamentoAlocandoAoEspaco(CriarEquipamentoEspacoDTO data) {
        return criarEquipamentoEspaco.criarEquipamentosAlocandoAoEspaco(data);
    }

    @Override
    public EquipamentoEspacoRetornoDTO inativar(String equipamentoEspacoId, String usuarioId) {
        return inativarEquipamentoEspaco.inativar(equipamentoEspacoId, usuarioId);
    }

    @Override
    public Page<EquipamentoEspacoRetornoDTO> obter(Pageable pageable, String id, String equipamentoId, String tipoEquipamentoId, String espacoId, LocalDateTime dataInicio, LocalDateTime dataFim, String tipoEquipamentoNome, String espacoNome) {
        return obterEquipamentosEspaco.obter(pageable, id, equipamentoId, tipoEquipamentoId, espacoId, dataInicio, dataFim, tipoEquipamentoNome, espacoNome);
    }
}
