package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Use case responsável por validar se um usuário é gestor ativo de um complexo de espaços.
 * Utilizado para controle de acesso em operações que requerem permissão de gestor.
 */
@Slf4j
@Component
public class ValidadorGestorComplexoEspacos {

    @Autowired
    private GestorComplexoEspacosRepository repository;

    /**
     * Valida se o usuário é gestor ativo do complexo de espaços.
     * Lança exceção caso o usuário não seja gestor ativo.
     *
     * @param usuarioId ID do usuário a ser validado
     * @param complexoEspacosId ID do complexo de espaços
     * @throws ValidationException se o usuário não for gestor ativo do complexo
     */
    public void validarGestorAtivo(String usuarioId, String complexoEspacosId) {
        log.debug("Validando se usuário {} é gestor ativo do complexo {}", usuarioId, complexoEspacosId);

        boolean ehGestor = repository.existsByUsuarioGestorIdAndComplexoEspacosIdAndEstaAtivoTrue(usuarioId, complexoEspacosId);
        if (!ehGestor) {
            log.warn("Validação falhou - Usuário {} não é gestor ativo do complexo {}", usuarioId, complexoEspacosId);
            throw new ValidationException("Usuário não é gestor ativo do complexo de espaços informado.");
        }

        log.debug("Validação bem-sucedida - Usuário {} é gestor ativo do complexo {}", usuarioId, complexoEspacosId);
    }
}
