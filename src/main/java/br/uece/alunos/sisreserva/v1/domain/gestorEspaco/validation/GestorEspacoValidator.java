package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.validation;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pelas validações de negócio relacionadas ao GestorEspaco.
 * Centraliza as regras de validação para garantir consistência e segurança dos dados.
 */
@Component
public class GestorEspacoValidator {
    @Autowired
    private GestorEspacoRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Valida se o usuário autenticado possui permissão para vincular gestor de espaço.
     * Apenas administradores podem realizar esta operação.
     * 
     * @throws ValidationException se o usuário não for administrador
     */
    public void validarPermissaoParaVincular() {
        if (!usuarioAutenticadoService.isAdmin()) {
            throw new ValidationException(
                "Apenas administradores podem vincular gestores de espaço."
            );
        }
    }

    /**
     * Valida se já existe um gestor ativo para o usuário e espaço especificados.
     * Lança exceção se já existir um vínculo ativo.
     * 
     * @param usuarioId ID do usuário
     * @param espacoId ID do espaço
     * @throws ValidationException se já existir um gestor ativo
     */
    public void validarGestorAtivoExistente(String usuarioId, String espacoId) {
        boolean existeAtivo = repository.existsByUsuarioGestorIdAndEspacoIdAndEstaAtivoTrue(usuarioId, espacoId);
        if (existeAtivo) {
            throw new ValidationException("O usuário já é gestor ativo do espaço.");
        }
    }

    /**
     * Valida se o gestor existe e está ativo antes de inativá-lo.
     * Lança exceção se o gestor não for encontrado ou já estiver inativo.
     * 
     * @param gestorEspacoId ID do gestor de espaço
     * @throws ValidationException se o gestor não existir ou já estiver inativo
     */
    public void validarGestorAtivoParaInativar(String gestorEspacoId) {
        var gestorEspaco = repository.findById(gestorEspacoId)
                .orElseThrow(() -> new ValidationException("Gestor do espaço não encontrado."));

        if (!gestorEspaco.getEstaAtivo()) {
            throw new ValidationException("O gestor já está inativo.");
        }
    }
}
