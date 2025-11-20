package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por validar se um usuário é membro ativo da secretaria de um espaço.
 * Pode ser utilizado em outras partes do sistema que precisam verificar permissões.
 */
@Component
public class ValidadorSecretariaEspaco {

    @Autowired
    private SecretariaEspacoRepository repository;

    /**
     * Valida se o usuário é membro ativo da secretaria do espaço especificado.
     * Lança exceção se o usuário não fizer parte da secretaria ativa.
     * 
     * @param usuarioId ID do usuário a ser validado
     * @param espacoId ID do espaço
     * @throws ValidationException se o usuário não for membro ativo da secretaria
     */
    public void validarSecretariaAtiva(String usuarioId, String espacoId) {
        boolean ehSecretaria = repository.existsByUsuarioSecretariaIdAndEspacoIdAndEstaAtivoTrue(usuarioId, espacoId);
        if (!ehSecretaria) {
            throw new ValidationException("Usuário não é membro ativo da secretaria do espaço informado.");
        }
    }
}
