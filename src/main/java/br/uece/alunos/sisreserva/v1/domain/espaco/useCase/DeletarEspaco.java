package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.validation.EspacoValidator;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para deletar um espaço.
 * Valida permissões e remove o espaço do sistema.
 */
@Component
@RequiredArgsConstructor
public class DeletarEspaco {
    
    private final EspacoRepository repository;
    private final EspacoValidator validator;
    
    /**
     * Deleta um espaço do sistema.
     * Apenas administradores podem realizar esta operação.
     * 
     * @param id identificador do espaço a ser deletado
     * @throws ValidationException se o usuário não tiver permissão ou o espaço não existir
     */
    public void deletar(String id) {
        // Valida permissão de administrador
        validator.validarPermissaoParaDeletar();
        
        // Valida se o espaço existe
        validator.validarEspacoId(id);
        
        // Remove o espaço (cascade remove relacionamentos)
        repository.deleteById(id);
    }
}
