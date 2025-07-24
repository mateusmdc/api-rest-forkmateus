package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.CriarEquipamentoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface EquipamentoEspacoService {
    List<EquipamentoEspacoRetornoDTO> criarEquipamentoAlocandoAoEspaco(CriarEquipamentoEspacoDTO data);
    EquipamentoEspacoRetornoDTO inativar(String equipamentoEspacoId, String usuarioId);
    List<EquipamentoEspacoRetornoDTO> inativarEmLote(List<String> equipamentoEspacoIds, String usuarioId);
    Page<EquipamentoEspacoRetornoDTO> obter(Pageable pageable,
                                            String id,
                                            String equipamentoId,
                                            String tipoEquipamentoId,
                                            String espacoId,
                                            LocalDateTime dataInicio,
                                            LocalDateTime dataFim,
                                            String tipoEquipamentoNome,
                                            String espacoNome);
}
