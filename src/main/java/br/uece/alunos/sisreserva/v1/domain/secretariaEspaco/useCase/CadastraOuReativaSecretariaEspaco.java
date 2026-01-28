package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.validation.SecretariaEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por cadastrar ou reativar uma secretaria de espaço.
 * Verifica se já existe um registro inativo antes de criar um novo.
 * Valida que apenas administradores podem realizar esta operação.
 */
@Component
public class CadastraOuReativaSecretariaEspaco {
    
    @Autowired
    private SecretariaEspacoRepository repository;

    @Autowired
    private SecretariaEspacoValidator validator;

    @Autowired
    private CadastrarSecretariaEspaco cadastrarSecretariaEspaco;

    @Autowired
    private ReativarSecretariaEspaco reativarSecretariaEspaco;

    /**
     * Executa a lógica de cadastro ou reativação de secretaria de espaço.
     * Se existir uma secretaria inativa para o usuário e espaço, reativa.
     * Caso contrário, cria uma nova secretaria.
     * 
     * @param data Dados do usuário e espaço a serem vinculados
     * @return DTO com os dados da secretaria cadastrada ou reativada
     * @throws br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException
     *         se o usuário não for administrador
     */
    public SecretariaEspacoRetornoDTO executar(SecretariaEspacoDTO data) {
        // Valida se o usuário autenticado tem permissão para vincular secretaria
        validator.validarPermissaoParaVincular();
        
        // Busca por uma secretaria inativa com os mesmos dados
        var secretariaInativaOpt = repository.findByUsuarioSecretariaIdAndEspacoIdAndEstaAtivoFalse(
            data.usuarioSecretariaId(), 
            data.espacoId()
        );

        // Se existir uma secretaria inativa, reativa
        if (secretariaInativaOpt.isPresent()) {
            return reativarSecretariaEspaco.reativar(secretariaInativaOpt.get());
        }

        // Caso contrário, cadastra uma nova
        return cadastrarSecretariaEspaco.cadastrarSecretariaEspaco(data);
    }
}
