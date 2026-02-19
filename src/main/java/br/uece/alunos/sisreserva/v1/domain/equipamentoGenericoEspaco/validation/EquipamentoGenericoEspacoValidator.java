package br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.validation;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pelas validações de negócio relacionadas ao EquipamentoGenericoEspaco.
 * Centraliza as regras de validação para garantir consistência e segurança dos dados.
 * 
 * <p>Validações de permissão:</p>
 * <ul>
 *   <li>Administradores têm permissão total</li>
 *   <li>Gestores de um espaço podem gerenciar equipamentos desse espaço</li>
 *   <li>Secretarias de um espaço podem gerenciar equipamentos desse espaço</li>
 * </ul>
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Component
public class EquipamentoGenericoEspacoValidator {

    @Autowired
    private EquipamentoGenericoEspacoRepository repository;

    @Autowired
    private GestorEspacoRepository gestorEspacoRepository;

    @Autowired
    private SecretariaEspacoRepository secretariaEspacoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Valida se o usuário autenticado possui permissão para gerenciar equipamentos do espaço.
     * Permissão é concedida se o usuário for:
     * <ul>
     *   <li>Administrador do sistema</li>
     *   <li>Gestor ativo do espaço</li>
     *   <li>Secretaria ativa do espaço</li>
     * </ul>
     * 
     * @param espacoId ID do espaço a ser verificado
     * @throws ValidationException se o usuário não possui permissão
     */
    public void validarPermissaoParaGerenciarEquipamentos(String espacoId) {
        Usuario usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        
        if (usuario == null) {
            throw new ValidationException("Usuário não autenticado.");
        }

        // Administradores têm permissão total
        if (usuarioAutenticadoService.isAdmin()) {
            return;
        }

        // Verifica se é gestor ativo do espaço
        boolean isGestor = gestorEspacoRepository
            .existsByUsuarioGestorIdAndEspacoIdAndEstaAtivoTrue(usuario.getId(), espacoId);
        
        if (isGestor) {
            return;
        }

        // Verifica se é secretaria ativa do espaço
        boolean isSecretaria = secretariaEspacoRepository
            .existsByUsuarioSecretariaIdAndEspacoIdAndEstaAtivoTrue(usuario.getId(), espacoId);
        
        if (isSecretaria) {
            return;
        }

        throw new ValidationException(
            "Você não tem permissão para gerenciar equipamentos deste espaço. " +
            "Apenas administradores, gestores ou secretarias do espaço podem realizar esta operação."
        );
    }

    /**
     * Valida se já existe um vínculo entre o equipamento genérico e o espaço.
     * 
     * @param equipamentoGenericoId ID do equipamento genérico
     * @param espacoId ID do espaço
     * @throws ValidationException se o vínculo já existe
     */
    public void validarVinculoJaExistente(String equipamentoGenericoId, String espacoId) {
        boolean existe = repository.existsByEquipamentoGenericoIdAndEspacoId(equipamentoGenericoId, espacoId);
        if (existe) {
            throw new ValidationException(
                "O equipamento genérico já está vinculado a este espaço. " +
                "Use a operação de atualização para modificar a quantidade."
            );
        }
    }

    /**
     * Valida se o vínculo existe antes de operações de atualização ou remoção.
     * 
     * @param vinculoId ID do vínculo equipamento-espaço
     * @throws ValidationException se o vínculo não existe
     */
    public void validarVinculoExiste(String vinculoId) {
        boolean existe = repository.existsById(vinculoId);
        if (!existe) {
            throw new ValidationException("Vínculo equipamento-espaço não encontrado.");
        }
    }

    /**
     * Valida se a quantidade informada é válida (maior que zero).
     * 
     * @param quantidade quantidade a ser validada
     * @throws ValidationException se a quantidade for inválida
     */
    public void validarQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade < 1) {
            throw new ValidationException("A quantidade deve ser no mínimo 1.");
        }
    }
}
