package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.specification.EspacoSpecification;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Caso de uso para obter espaços reserváveis com filtros e paginação.
 * 
 * <p>Este caso de uso retorna apenas espaços com o campo 'reservavel' definido como true.</p>
 * 
 * <p>Aplica restrições de visualização baseadas no cargo do usuário autenticado:</p>
 * <ul>
 *   <li><strong>Usuários externos (USUARIO_EXTERNO)</strong>: Visualizam apenas espaços reserváveis 
 *       que também possuem o campo 'multiusuario' como true.</li>
 *   <li><strong>Usuários internos e administradores</strong>: Visualizam todos os espaços reserváveis, 
 *       independentemente do campo 'multiusuario'.</li>
 * </ul>
 * 
 * <p>Os demais filtros (departamento, localização, tipo de espaço, etc.) funcionam normalmente.</p>
 */
@Slf4j
@Component
@AllArgsConstructor
public class ObterEspacosReservaveis {

    private final EspacoRepository espacoRepository;
    private final UtilsService utilsService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Obtém espaços reserváveis com filtros e paginação.
     * 
     * <p>Força o filtro 'reservavel = true' e aplica automaticamente restrição 
     * 'multiusuario = true' para usuários externos.</p>
     * 
     * @param pageable Informações de paginação e ordenação
     * @param id Filtro por ID do espaço (opcional)
     * @param departamento Filtro por ID do departamento (opcional)
     * @param localizacao Filtro por ID da localização (opcional)
     * @param tipoEspaco Filtro por ID do tipo de espaço (opcional)
     * @param tipoAtividade Filtro por ID do tipo de atividade (opcional)
     * @param nome Filtro por nome do espaço - busca normalizada ignorando acentos (opcional)
     * @param multiusuario Filtro explícito por espaços multiusuário - sobrescrito para true se usuário for externo (opcional)
     * @return Página com os espaços reserváveis encontrados
     */
    public Page<EspacoRetornoDTO> obterEspacosReservaveis(Pageable pageable,
                                                          String id,
                                                          String departamento,
                                                          String localizacao,
                                                          String tipoEspaco,
                                                          String tipoAtividade,
                                                          String nome,
                                                          Boolean multiusuario) {

        // Monta mapa de filtros
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (departamento != null) filtros.put("departamentoId", departamento);
        if (localizacao != null) filtros.put("localizacaoId", localizacao);
        if (tipoEspaco != null) filtros.put("tipoEspacoId", tipoEspaco);
        if (tipoAtividade != null) filtros.put("tipoAtividadeId", tipoAtividade);
        if (multiusuario != null) filtros.put("multiusuario", multiusuario);
        
        // FORÇA o filtro reservavel = true
        filtros.put("reservavel", true);

        // Verifica se o usuário autenticado é externo e deve ter restrições
        boolean restringirApenasMultiusuario = usuarioAutenticadoService.deveAplicarRestricoesMultiusuario();

        // Log de auditoria: registra quando filtro de restrição é aplicado
        if (restringirApenasMultiusuario) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            if (usuario != null) {
                log.info("[AUDIT] FILTRO_RESERVAVEIS_APLICADO - Usuário externo '{}' (ID: {}) listando espaços reserváveis - Restrições: reservavel=true, multiusuario=true",
                        usuario.getEmail(), usuario.getId());
            }
        } else {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            if (usuario != null) {
                log.info("[AUDIT] FILTRO_RESERVAVEIS_APLICADO - Usuário '{}' (ID: {}) listando espaços reserváveis - Restrições: reservavel=true",
                        usuario.getEmail(), usuario.getId());
            }
        }

        // Cria specification com os filtros
        var spec = EspacoSpecification.byFilter(
                (String) filtros.get("id"),
                (String) filtros.get("departamentoId"),
                (String) filtros.get("localizacaoId"),
                (String) filtros.get("tipoEspacoId"),
                (String) filtros.get("tipoAtividadeId"),
                (Boolean) filtros.get("multiusuario"),
                (Boolean) filtros.get("reservavel"),
                restringirApenasMultiusuario  // Restrição adicional para usuários externos
        );

        // Busca todos os resultados (sem paginação inicial para filtrar por nome)
        var results = espacoRepository.findAll(spec, Sort.unsorted());

        // Aplica filtro de nome normalizado, se fornecido
        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = utilsService.normalizeString(nome);
            results = results.stream()
                    .filter(e -> utilsService.normalizeString(e.getNome()).contains(nomeBusca))
                    .toList();
        }

        // Calcula paginação manual
        int total = results.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        // Cria página com os resultados
        List<EspacoRetornoDTO> page = results.subList(start, end)
                .stream()
                .map(EspacoRetornoDTO::new)
                .toList();

        // Log de auditoria: registra quantidade de resultados
        if (restringirApenasMultiusuario) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            if (usuario != null) {
                log.info("[AUDIT] RESULTADO_RESERVAVEIS - Usuário externo '{}' visualizou {} espaços reserváveis e multiusuário (total de espaços reserváveis no sistema pode ser maior)",
                        usuario.getEmail(), total);
            }
        } else {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            if (usuario != null) {
                log.info("[AUDIT] RESULTADO_RESERVAVEIS - Usuário '{}' visualizou {} espaços reserváveis",
                        usuario.getEmail(), total);
            }
        }

        return new PageImpl<>(page, pageable, total);
    }
}
