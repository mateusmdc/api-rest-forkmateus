package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.ValidadorGestorEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.VincularEquipamentoEspacoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Caso de uso para editar o vínculo de um equipamento, transferindo-o de um espaço para outro.
 *
 * <p>Inativa todos os vínculos ativos do equipamento e cria um novo vínculo com o espaço
 * especificado. O usuário deve ser gestor dos espaços envolvidos.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EditarVinculoEquipamentoEspaco {

    private final EquipamentoEspacoRepository repository;
    private final ValidadorGestorEspaco validaSeGestorEspaco;
    private final VincularEquipamentoEspaco vincularEquipamentoEspaco;

    /**
     * Transfere um equipamento para um novo espaço, encerrando todos os vínculos ativos
     * e criando um novo.
     *
     * @param dto dados da vinculação (equipamentoId, espacoId, usuarioId)
     * @return DTO do novo vínculo criado
     */
    @Transactional
    public EquipamentoEspacoRetornoDTO executar(VincularEquipamentoEspacoDTO dto) {
        var vinculosAtivos = repository.findByEquipamentoIdAndDataRemocaoIsNull(dto.equipamentoId());

        if (!vinculosAtivos.isEmpty()) {
            var agora = LocalDateTime.now();
            for (var vinculo : vinculosAtivos) {
                validaSeGestorEspaco.validarGestorAtivo(dto.usuarioId(), vinculo.getEspaco().getId());
                vinculo.setDataRemocao(agora);
            }
            repository.saveAll(vinculosAtivos);
        }

        var novoVinculo = vincularEquipamentoEspaco.executar(dto);

        log.info("[AUDIT] VINCULO_EDITADO - Equipamento ID '{}' transferido para espaço ID '{}' pelo usuário ID: {}",
                dto.equipamentoId(), dto.espacoId(), dto.usuarioId());

        return new EquipamentoEspacoRetornoDTO(novoVinculo);
    }
}
