package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.validation;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validador de regras de negócio para ComplexoEspacos.
 * Inclui validações de existência e permissões de gestor.
 */
@Slf4j
@Component
@AllArgsConstructor
public class ComplexoEspacosValidator {
    private final ComplexoEspacosRepository repository;
    private final GestorComplexoEspacosRepository gestorComplexoEspacosRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public void validarSeComplexoJaExiste(String nome) {
        var complexoExistente = repository.findByNomeIgnoreCase(nome);
        if (complexoExistente.isPresent()) {
            throw new ValidationException("Já existe um complexo de espaços com este nome");
        }
    }

    public void validarSeComplexoJaExisteParaAtualizacao(String id, String nome) {
        if (nome == null || nome.isBlank()) {
            return;
        }
        var complexoExistente = repository.findByNomeIgnoreCase(nome);
        if (complexoExistente.isPresent() && !complexoExistente.get().getId().equals(id)) {
            throw new ValidationException("Já existe outro complexo de espaços com este nome");
        }
    }

    /**
     * Valida se o usuário autenticado tem permissão para modificar o complexo.
     * Apenas administradores ou gestores do complexo podem realizar alterações.
     *
     * @param complexoEspacosId ID do complexo de espaços
     * @throws ValidationException se o usuário não tiver permissão
     */
    public void validarPermissaoParaModificarComplexo(String complexoEspacosId) {
        var usuarioAutenticado = usuarioAutenticadoService.getUsuarioAutenticado();
        
        // Administradores têm permissão total
        if (usuarioAutenticadoService.isAdmin()) {
            log.debug("Usuário {} é ADMIN - permissão concedida para modificar complexo {}", 
                    usuarioAutenticado.getId(), complexoEspacosId);
            return;
        }
        
        // Verifica se é gestor do complexo
        boolean isGestor = gestorComplexoEspacosRepository
                .existsByUsuarioGestorIdAndComplexoEspacosIdAndEstaAtivoTrue(
                        usuarioAutenticado.getId(), complexoEspacosId);
        
        if (!isGestor) {
            log.warn("Acesso negado - Usuário {} não tem permissão para modificar complexo {}", 
                    usuarioAutenticado.getId(), complexoEspacosId);
            throw new ValidationException("Apenas administradores ou gestores do complexo podem realizar esta operação.");
        }
        
        log.debug("Usuário {} é gestor do complexo {} - permissão concedida", 
                usuarioAutenticado.getId(), complexoEspacosId);
    }

    /**
     * Valida se o usuário autenticado é ADMIN.
     * Apenas administradores podem criar ou deletar complexos de espaços.
     *
     * @throws ValidationException se o usuário não for ADMIN
     */
    public void validarPermissaoAdmin() {
        if (!usuarioAutenticadoService.isAdmin()) {
            var usuarioAutenticado = usuarioAutenticadoService.getUsuarioAutenticado();
            log.warn("Acesso negado - Usuário {} não é ADMIN", usuarioAutenticado.getId());
            throw new ValidationException("Apenas administradores podem realizar esta operação.");
        }
    }

    /**
     * Valida se o usuário autenticado tem permissão para listar complexos.
     * Apenas administradores ou usuários internos podem listar.
     *
     * @throws ValidationException se o usuário não tiver permissão
     */
    public void validarPermissaoParaListar() {
        var usuarioAutenticado = usuarioAutenticadoService.getUsuarioAutenticado();
        
        // Administradores têm permissão total
        if (usuarioAutenticadoService.isAdmin()) {
            log.debug("Usuário {} é ADMIN - permissão concedida para listar complexos", usuarioAutenticado.getId());
            return;
        }
        
        // Usuários externos não podem listar
        if (usuarioAutenticadoService.deveAplicarRestricoesMultiusuario()) {
            log.warn("Acesso negado - Usuário externo {} tentou listar complexos", usuarioAutenticado.getId());
            throw new ValidationException("Apenas administradores ou usuários internos podem listar complexos de espaços.");
        }
        
        log.debug("Usuário interno {} - permissão concedida para listar complexos", usuarioAutenticado.getId());
    }
}

