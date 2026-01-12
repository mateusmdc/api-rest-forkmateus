package br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenerico;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenericoRepository;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para criar um novo equipamento genérico.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class CriarEquipamentoGenerico {

    private final EquipamentoGenericoRepository repository;

    /**
     * Cria um novo equipamento genérico no sistema.
     * Valida se já existe um equipamento com o mesmo nome.
     * 
     * @param dto DTO contendo os dados do equipamento genérico a ser criado
     * @return Equipamento genérico criado
     * @throws ValidationException se já existir equipamento com o mesmo nome
     */
    public EquipamentoGenerico criar(EquipamentoGenericoDTO dto) {
        log.info("Criando novo equipamento genérico: {}", dto.nome());
        
        // Valida se já existe equipamento com o mesmo nome
        if (repository.existsByNomeIgnoreCaseAndTrimmed(dto.nome())) {
            log.warn("Tentativa de criar equipamento genérico com nome duplicado: {}", dto.nome());
            throw new ValidationException("Já existe um equipamento genérico com o nome: " + dto.nome());
        }
        
        EquipamentoGenerico equipamento = new EquipamentoGenerico(dto);
        EquipamentoGenerico saved = repository.save(equipamento);
        
        log.info("Equipamento genérico criado com sucesso. ID: {}", saved.getId());
        return saved;
    }
}
