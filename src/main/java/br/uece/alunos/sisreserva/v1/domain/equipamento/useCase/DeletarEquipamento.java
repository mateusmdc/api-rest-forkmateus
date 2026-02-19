package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamento.validation.EquipamentoValidator;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para deletar um equipamento.
 * Valida permissões e remove o equipamento do sistema.
 */
@Component
@RequiredArgsConstructor
public class DeletarEquipamento {
    
    private final EquipamentoRepository repository;
    private final EquipamentoValidator validator;
    
    /**
     * Deleta um equipamento do sistema.
     * Apenas administradores podem realizar esta operação.
     * 
     * @param id identificador do equipamento a ser deletado
     * @throws ValidationException se o usuário não tiver permissão ou o equipamento não existir
     */
    public void deletar(String id) {
        // Valida permissão de administrador
        validator.validarPermissaoParaDeletar();
        
        // Valida se o equipamento existe
        validator.validarEquipamentoId(id);
        
        // Remove o equipamento (cascade remove relacionamentos em equipamento_espaco)
        repository.deleteById(id);
    }
}
