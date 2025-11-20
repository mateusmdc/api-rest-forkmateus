package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.validation;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pelas validações de negócio relacionadas à SecretariaEspaco.
 * Centraliza as regras de validação para garantir consistência dos dados.
 */
@Component
public class SecretariaEspacoValidator {
    
    @Autowired
    private SecretariaEspacoRepository repository;

    /**
     * Valida se já existe uma secretaria ativa para o usuário e espaço especificados.
     * Lança exceção se já existir um vínculo ativo.
     * 
     * @param usuarioId ID do usuário
     * @param espacoId ID do espaço
     * @throws ValidationException se já existir uma secretaria ativa
     */
    public void validarSecretariaAtivaExistente(String usuarioId, String espacoId) {
        boolean existeAtivo = repository.existsByUsuarioSecretariaIdAndEspacoIdAndEstaAtivoTrue(usuarioId, espacoId);
        if (existeAtivo) {
            throw new ValidationException("O usuário já faz parte da secretaria ativa do espaço.");
        }
    }

    /**
     * Valida se a secretaria existe e está ativa antes de inativá-la.
     * Lança exceção se a secretaria não for encontrada ou já estiver inativa.
     * 
     * @param secretariaEspacoId ID da secretaria de espaço
     * @throws ValidationException se a secretaria não existir ou já estiver inativa
     */
    public void validarSecretariaAtivaParaInativar(String secretariaEspacoId) {
        var secretariaEspaco = repository.findById(secretariaEspacoId)
                .orElseThrow(() -> new ValidationException("Secretaria do espaço não encontrada."));

        if (!secretariaEspaco.getEstaAtivo()) {
            throw new ValidationException("A secretaria já está inativa.");
        }
    }
}
