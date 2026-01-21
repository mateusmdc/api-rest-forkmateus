package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.validation.EquipamentoEspacoValidator;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.VincularEquipamentoEspacoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Caso de uso para vincular um equipamento existente a um espaço existente.
 * 
 * <p>Valida permissões do usuário (deve ser gestor do espaço) e verifica
 * se já não existe um vínculo ativo entre o equipamento e o espaço.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VincularEquipamentoEspaco {

    private final EquipamentoEspacoRepository repository;
    private final EntityHandlerService entityHandlerService;
    private final EquipamentoEspacoValidator validator;

    /**
     * Vincula um equipamento existente a um espaço existente.
     * 
     * @param dto dados da vinculação
     * @return EquipamentoEspaco criado
     * @throws ValidationException se o usuário não for gestor ou se já existir vínculo ativo
     */
    public EquipamentoEspaco executar(VincularEquipamentoEspacoDTO dto) {
        // Obter entidades
        Equipamento equipamento = entityHandlerService.obterEquipamentoPorId(dto.equipamentoId());
        Espaco espaco = entityHandlerService.obterEspacoPorId(dto.espacoId());
        
        // Validar permissão do usuário (deve ser gestor do espaço)
        validator.validarPermissaoGestor(dto.usuarioId(), dto.espacoId());
        
        // Verificar se já não existe um vínculo ativo
        var vinculoExistente = repository.findByEquipamentoIdAndDataRemocaoIsNull(dto.equipamentoId());
        
        if (vinculoExistente != null && !vinculoExistente.isEmpty()) {
            var vinculo = vinculoExistente.get(0);
            
            // Se o vínculo é com o mesmo espaço, retornar erro
            if (vinculo.getEspaco().getId().equals(dto.espacoId())) {
                log.warn("[VALIDATION] Tentativa de vincular equipamento '{}' ao espaço '{}', mas vínculo já existe",
                        dto.equipamentoId(), dto.espacoId());
                throw new ValidationException(
                    "Este equipamento já está vinculado a este espaço. " +
                    "Para realocar o equipamento, primeiro inative o vínculo atual."
                );
            }
            
            // Se o vínculo é com outro espaço, retornar erro informando
            log.warn("[VALIDATION] Tentativa de vincular equipamento '{}' ao espaço '{}', mas já está vinculado ao espaço '{}'",
                    dto.equipamentoId(), dto.espacoId(), vinculo.getEspaco().getId());
            throw new ValidationException(
                String.format(
                    "Este equipamento já está vinculado ao espaço '%s' (ID: %s). " +
                    "Para realocar o equipamento, primeiro inative o vínculo atual.",
                    vinculo.getEspaco().getNome(),
                    vinculo.getEspaco().getId()
                )
            );
        }
        
        // Criar novo vínculo
        EquipamentoEspaco novoVinculo = new EquipamentoEspaco();
        novoVinculo.setEquipamento(equipamento);
        novoVinculo.setEspaco(espaco);
        // dataAlocacao é setada automaticamente pelo @PrePersist
        novoVinculo.setDataRemocao(null);
        
        var vinculoSalvo = repository.save(novoVinculo);
        
        log.info("[AUDIT] EQUIPAMENTO_VINCULADO - Equipamento '{}' (ID: {}) vinculado ao espaço '{}' (ID: {}) pelo usuário ID: {}",
                equipamento.getTombamento(), equipamento.getId(),
                espaco.getNome(), espaco.getId(),
                dto.usuarioId());
        
        return vinculoSalvo;
    }
}
