package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.specification.EspacoSpecification;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Caso de uso para obter espaços com filtros e paginação.
 * Aplica restrições de visualização baseadas no cargo do usuário autenticado.
 * Usuários externos só podem visualizar espaços multiusuário.
 */
@Component
@AllArgsConstructor
public class ObterEspaco {

    private final EspacoRepository espacoRepository;
    private final UtilsService utilsService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Obtém espaços com filtros e paginação.
     * Aplica automaticamente restrição para usuários externos (apenas espaços multiusuário).
     * 
     * @param pageable Informações de paginação
     * @param id Filtro por ID do espaço
     * @param departamento Filtro por ID do departamento
     * @param localizacao Filtro por ID da localização
     * @param tipoEspaco Filtro por ID do tipo de espaço
     * @param tipoAtividade Filtro por ID do tipo de atividade
     * @param nome Filtro por nome do espaço (busca normalizada)
     * @param multiusuario Filtro explícito por espaços multiusuário
     * @return Página com os espaços encontrados
     */
    public Page<EspacoRetornoDTO> obterEspacos(Pageable pageable,
                                               String id,
                                               String departamento,
                                               String localizacao,
                                               String tipoEspaco,
                                               String tipoAtividade,
                                               String nome,
                                               Boolean multiusuario) {

        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (departamento != null) filtros.put("departamentoId", departamento);
        if (localizacao != null) filtros.put("localizacaoId", localizacao);
        if (tipoEspaco != null) filtros.put("tipoEspacoId", tipoEspaco);
        if (tipoAtividade != null) filtros.put("tipoAtividadeId", tipoAtividade);
        if (multiusuario != null) filtros.put("multiusuario", multiusuario);

        // Verifica se o usuário autenticado é externo e deve ter restrições
        boolean restringirApenasMultiusuario = usuarioAutenticadoService.deveRestringirEspacos();

        var spec = EspacoSpecification.byFilter(
                (String) filtros.get("id"),
                (String) filtros.get("departamentoId"),
                (String) filtros.get("localizacaoId"),
                (String) filtros.get("tipoEspacoId"),
                (String) filtros.get("tipoAtividadeId"),
                (Boolean) filtros.get("multiusuario"),
                restringirApenasMultiusuario  // Novo parâmetro para restrição de usuários externos
        );

        var results = espacoRepository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = utilsService.normalizeString(nome);
            results = results.stream()
                    .filter(e -> utilsService.normalizeString(e.getNome()).contains(nomeBusca))
                    .toList();
        }

        int total = results.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<EspacoRetornoDTO> page = results.subList(start, end)
                .stream()
                .map(EspacoRetornoDTO::new)
                .toList();

        return new PageImpl<>(page, pageable, total);
    }
}
