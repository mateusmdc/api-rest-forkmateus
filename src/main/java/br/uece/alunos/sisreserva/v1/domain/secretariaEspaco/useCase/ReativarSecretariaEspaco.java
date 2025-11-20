package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspaco;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por reativar uma secretaria de espaço que estava inativa.
 * Define o status como ativo e remove a data de inativação.
 */
@Component
public class ReativarSecretariaEspaco {
    
    @Autowired
    private SecretariaEspacoRepository repository;

    /**
     * Reativa uma secretaria de espaço inativa.
     * 
     * @param secretariaInativa Entidade da secretaria a ser reativada
     * @return DTO com os dados da secretaria reativada
     */
    public SecretariaEspacoRetornoDTO reativar(SecretariaEspaco secretariaInativa) {
        // Define o status como ativo
        secretariaInativa.setEstaAtivo(true);
        secretariaInativa.setDeletedAt(null);

        // Persiste a atualização no banco de dados
        var secretariaReativadaNoBanco = repository.save(secretariaInativa);

        return new SecretariaEspacoRetornoDTO(secretariaReativadaNoBanco);
    }
}
