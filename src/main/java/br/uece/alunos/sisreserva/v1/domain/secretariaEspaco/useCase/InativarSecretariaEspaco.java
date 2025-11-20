package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.validation.SecretariaEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por inativar uma secretaria de espaço.
 * Define o status como inativo, mantendo o registro no banco de dados.
 */
@Component
@AllArgsConstructor
public class InativarSecretariaEspaco {
    
    private final SecretariaEspacoRepository repository;
    private final SecretariaEspacoValidator validator;

    /**
     * Inativa uma secretaria de espaço.
     * 
     * @param secretariaEspacoId ID da secretaria a ser inativada
     * @return DTO com os dados da secretaria inativada
     * @throws br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException
     *         se a secretaria não for encontrada ou já estiver inativa
     */
    public SecretariaEspacoRetornoDTO inativar(String secretariaEspacoId) {
        // Valida se a secretaria existe e está ativa
        validator.validarSecretariaAtivaParaInativar(secretariaEspacoId);

        // Busca a secretaria no banco
        var secretaria = repository.findById(secretariaEspacoId)
                .orElseThrow(() -> new IllegalStateException(
                    "Secretaria de Espaço deveria existir após validação, mas não foi encontrada."
                ));

        // Define como inativa
        secretaria.setEstaAtivo(false);

        // Persiste a atualização
        var secretariaInativada = repository.save(secretaria);

        return new SecretariaEspacoRetornoDTO(secretariaInativada);
    }
}
