package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.validation;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EquipamentoEspacoValidator {
    @Autowired
    private EquipamentoEspacoRepository repository;
    
    @Autowired
    private GestorEspacoRepository gestorEspacoRepository;
    
    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    public void validarEquipamentoEspacoId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("O ID do equipamento espaço não pode ser nulo ou vazio.");
        }

        if (!repository.existsById(id)) {
            throw new ValidationException("Equipamento alocado ao espaço com o ID fornecido não existe.");
        }
    }
    
    /**
     * Valida se o usuário é gestor do espaço.
     * Apenas gestores ativos podem vincular/desvincular equipamentos.
     * 
     * @param usuarioId ID do usuário
     * @param espacoId ID do espaço
     * @throws ValidationException se o usuário não for gestor ativo do espaço
     */
    public void validarPermissaoGestor(String usuarioId, String espacoId) {
        // Admin sempre tem permissão
        if (usuarioAutenticadoService.isAdmin()) {
            return;
        }
        
        // Verifica se é gestor ativo do espaço
        boolean isGestorAtivo = gestorEspacoRepository.existsByUsuarioGestorIdAndEspacoIdAndEstaAtivoTrue(
                usuarioId, espacoId);
        
        if (!isGestorAtivo) {
            log.warn("[VALIDATION] Usuário '{}' tentou vincular equipamento ao espaço '{}' sem ser gestor ativo",
                    usuarioId, espacoId);
            throw new ValidationException(
                "Apenas gestores ativos do espaço podem vincular equipamentos. " +
                "Você não possui permissão para esta operação."
            );
        }
    }
}
