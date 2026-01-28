package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.validation.GestorEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por cadastrar ou reativar um gestor de espaço.
 * Verifica se já existe um registro inativo antes de criar um novo.
 * Valida que apenas administradores podem realizar esta operação.
 */
@Component
public class CadastraOuReativaGestorEspaco {
    @Autowired
    private GestorEspacoRepository repository;

    @Autowired
    private GestorEspacoValidator validator;

    @Autowired
    private CadastrarGestorEspaco cadastrarGestorEspaco;

    @Autowired
    private ReativarGestorEspaco reativarGestorEspaco;

    /**
     * Executa a lógica de cadastro ou reativação de gestor de espaço.
     * Se existir um gestor inativo para o usuário e espaço, reativa.
     * Caso contrário, cria um novo gestor.
     * 
     * @param data Dados do usuário e espaço a serem vinculados
     * @return DTO com os dados do gestor cadastrado ou reativado
     * @throws br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException
     *         se o usuário não for administrador
     */
    public GestorEspacoRetornoDTO executar(GestorEspacoDTO data) {
        // Valida se o usuário autenticado tem permissão para vincular gestor
        validator.validarPermissaoParaVincular();
        var gestorInativoOpt = repository.findByUsuarioGestorIdAndEspacoIdAndEstaAtivoFalse(data.usuarioGestorId(), data.espacoId());

        if (gestorInativoOpt.isPresent()) {
            return reativarGestorEspaco.reativar(gestorInativoOpt.get());
        }

        return cadastrarGestorEspaco.cadastrarGestorEspaco(data);
    }
}
