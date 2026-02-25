package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamento.specification.EquipamentoSpecification;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para obter equipamentos reserváveis com filtros e paginação.
 * 
 * <p>Este caso de uso retorna apenas equipamentos com o campo 'reservavel' definido como true.</p>
 * 
 * <p>Aplica restrições de visualização baseadas no cargo do usuário autenticado:</p>
 * <ul>
 *   <li><strong>Usuários externos (USUARIO_EXTERNO)</strong>: Visualizam apenas equipamentos reserváveis 
 *       que também possuem o campo 'multiusuario' como true.</li>
 *   <li><strong>Usuários internos e administradores</strong>: Visualizam todos os equipamentos reserváveis, 
 *       independentemente do campo 'multiusuario'.</li>
 * </ul>
 * 
 * <p>Os demais filtros (tombamento, status, tipo de equipamento, etc.) funcionam normalmente.</p>
 */
@Slf4j
@Component
@AllArgsConstructor
public class ObterEquipamentosReservaveis {

    private final EquipamentoRepository repository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Obtém equipamentos reserváveis com filtros e paginação.
     * 
     * <p>Força o filtro 'reservavel = true' e aplica automaticamente restrição 
     * 'multiusuario = true' para usuários externos.</p>
     * 
     * @param pageable Informações de paginação e ordenação
     * @param id Filtro por ID do equipamento (opcional)
     * @param tombamento Filtro por tombamento do equipamento (opcional)
     * @param status Filtro por status do equipamento (opcional)
     * @param tipoEquipamento Filtro por ID do tipo de equipamento (opcional)
     * @param multiusuario Filtro explícito por equipamentos multiusuário - sobrescrito para true se usuário for externo (opcional)
     * @return Página com os equipamentos reserváveis encontrados
     */
    public Page<EquipamentoRetornoDTO> obterEquipamentosReservaveis(Pageable pageable, 
                                                                     String id, 
                                                                     String tombamento, 
                                                                     String status, 
                                                                     String tipoEquipamento,
                                                                     Boolean multiusuario) {
        
        // Verifica se o usuário autenticado é externo e deve ter restrições
        boolean restringirApenasMultiusuario = usuarioAutenticadoService.deveAplicarRestricoesMultiusuario();

        // Log de auditoria: registra quando filtro de restrição é aplicado
        if (restringirApenasMultiusuario) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            if (usuario != null) {
                log.info("[AUDIT] FILTRO_RESERVAVEIS_APLICADO - Usuário externo '{}' (ID: {}) listando equipamentos reserváveis - Restrições: reservavel=true, multiusuario=true",
                        usuario.getEmail(), usuario.getId());
            }
        } else {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            if (usuario != null) {
                log.info("[AUDIT] FILTRO_RESERVAVEIS_APLICADO - Usuário '{}' (ID: {}) listando equipamentos reserváveis - Restrições: reservavel=true",
                        usuario.getEmail(), usuario.getId());
            }
        }

        // Cria specification com os filtros
        // FORÇA reservavel = true
        var spec = EquipamentoSpecification.byFilters(
                id,
                tombamento,
                status,
                tipoEquipamento,
                multiusuario,  // Passa o multiusuario recebido (pode ser null)
                true,  // FORÇA reservavel = true
                restringirApenasMultiusuario  // Restrição adicional para usuários externos
        );

        // Busca com paginação
        var page = repository.findAll(spec, pageable);

        // Log de auditoria: registra quantidade de resultados
        if (restringirApenasMultiusuario) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            if (usuario != null) {
                log.info("[AUDIT] RESULTADO_RESERVAVEIS - Usuário externo '{}' visualizou {} equipamentos reserváveis e multiusuário (total de equipamentos reserváveis no sistema pode ser maior)",
                        usuario.getEmail(), page.getTotalElements());
            }
        } else {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            if (usuario != null) {
                log.info("[AUDIT] RESULTADO_RESERVAVEIS - Usuário '{}' visualizou {} equipamentos reserváveis",
                        usuario.getEmail(), page.getTotalElements());
            }
        }

        return page.map(EquipamentoRetornoDTO::new);
    }
}
