package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.validation;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validador de regras de negócio para a entidade GestorComplexoEspacos.
 * Centraliza validações relacionadas a gestores de complexos de espaços.
 */
@Slf4j
@Component
public class GestorComplexoEspacosValidator {
    @Autowired
    private GestorComplexoEspacosRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Valida se um usuário já é gestor ativo do complexo de espaços.
     * Lança exceção caso o gestor já exista ativo.
     *
     * @param usuarioId ID do usuário
     * @param complexoEspacosId ID do complexo de espaços
     * @throws ValidationException se o usuário já for gestor ativo do complexo
     */
    public void validarGestorAtivoExistente(String usuarioId, String complexoEspacosId) {
        boolean existeAtivo = repository.existsByUsuarioGestorIdAndComplexoEspacosIdAndEstaAtivoTrue(usuarioId, complexoEspacosId);
        if (existeAtivo) {
            log.warn("Tentativa de criar gestor duplicado - Usuário: {} já é gestor ativo do complexo: {}", usuarioId, complexoEspacosId);
            throw new ValidationException("O usuário já é gestor ativo do complexo de espaços.");
        }
    }

    /**
     * Valida se o gestor está ativo antes de inativá-lo.
     * Lança exceção caso o gestor já esteja inativo.
     *
     * @param gestorComplexoEspacosId ID do gestor do complexo de espaços
     * @throws ValidationException se o gestor não for encontrado ou já estiver inativo
     */
    public void validarGestorAtivoParaInativar(String gestorComplexoEspacosId) {
        var gestorComplexoEspacos = repository.findById(gestorComplexoEspacosId)
                .orElseThrow(() -> new ValidationException("Gestor do complexo de espaços não encontrado."));

        if (!gestorComplexoEspacos.getEstaAtivo()) {
            log.warn("Tentativa de inativar gestor já inativo - ID: {}", gestorComplexoEspacosId);
            throw new ValidationException("O gestor do complexo já está inativo.");
        }
    }

    /**
     * Valida se o usuário tem permissão de gestor ativo para o complexo de espaços.
     * Lança exceção caso o usuário não seja gestor ativo do complexo.
     *
     * @param usuarioId ID do usuário
     * @param complexoEspacosId ID do complexo de espaços
     * @throws ValidationException se o usuário não for gestor ativo do complexo
     */
    public void validarPermissaoGestor(String usuarioId, String complexoEspacosId) {
        boolean isGestor = repository.existsByUsuarioGestorIdAndComplexoEspacosIdAndEstaAtivoTrue(usuarioId, complexoEspacosId);
        if (!isGestor) {
            log.warn("Acesso negado - Usuário: {} não é gestor do complexo: {}", usuarioId, complexoEspacosId);
            throw new ValidationException("Apenas gestores do complexo podem realizar esta operação.");
        }
    }

    /**
     * Valida se o usuário autenticado é ADMIN.
     * Apenas administradores podem gerenciar gestores de complexos de espaços.
     *
     * @throws ValidationException se o usuário não for ADMIN
     */
    public void validarPermissaoAdmin() {
        if (!usuarioAutenticadoService.isAdmin()) {
            var usuarioAutenticado = usuarioAutenticadoService.getUsuarioAutenticado();
            log.warn("Acesso negado - Usuário {} não é ADMIN para gerenciar gestores de complexo", usuarioAutenticado.getId());
            throw new ValidationException("Apenas administradores podem gerenciar gestores de complexos de espaços.");
        }
    }
}
