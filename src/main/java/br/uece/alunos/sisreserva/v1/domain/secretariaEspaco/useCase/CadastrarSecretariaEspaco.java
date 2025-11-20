package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspaco;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.validation.SecretariaEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por cadastrar uma nova secretaria de espaço.
 * Valida se já existe um vínculo ativo antes de criar um novo.
 */
@Component
public class CadastrarSecretariaEspaco {
    
    @Autowired
    private SecretariaEspacoRepository repository;

    @Autowired
    private EntityHandlerService entityHandlerService;

    @Autowired
    private SecretariaEspacoValidator validator;

    /**
     * Cadastra uma nova secretaria de espaço.
     * 
     * @param data Dados do usuário e espaço a serem vinculados
     * @return DTO com os dados da secretaria cadastrada
     * @throws br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException 
     *         se já existir um vínculo ativo ou se o usuário/espaço não forem encontrados
     */
    public SecretariaEspacoRetornoDTO cadastrarSecretariaEspaco(SecretariaEspacoDTO data) {
        // Valida se já existe uma secretaria ativa para este usuário e espaço
        validator.validarSecretariaAtivaExistente(data.usuarioSecretariaId(), data.espacoId());

        // Busca o usuário e espaço no banco de dados
        var usuarioSecretaria = entityHandlerService.obterUsuarioPorId(data.usuarioSecretariaId());
        var espaco = entityHandlerService.obterEspacoPorId(data.espacoId());

        // Cria a nova secretaria de espaço
        var novaSecretaria = new SecretariaEspaco(usuarioSecretaria, espaco);

        // Persiste no banco de dados
        var secretariaEspacoSalva = repository.save(novaSecretaria);

        return new SecretariaEspacoRetornoDTO(secretariaEspacoSalva);
    }
}
