package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.specification.SecretariaEspacoSpecification;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Caso de uso responsável por obter secretarias de espaço com filtros e paginação.
 * Permite buscar por ID, espaço, usuário e incluir ou não registros inativos.
 */
@Component
@AllArgsConstructor
public class ObterSecretariaEspaco {
    
    private final SecretariaEspacoRepository repository;

    /**
     * Obtém secretarias de espaço com filtros e paginação.
     * 
     * @param pageable Informações de paginação e ordenação
     * @param id Filtro por ID da secretaria (opcional)
     * @param espacoId Filtro por ID do espaço (opcional)
     * @param secretariaId Filtro por ID do usuário da secretaria (opcional)
     * @param todos Se true, inclui registros inativos; se false, apenas ativos
     * @return Página com os dados das secretarias encontradas
     */
    public Page<SecretariaEspacoRetornoDTO> obter(
            Pageable pageable, 
            String id, 
            String espacoId, 
            String secretariaId, 
            Boolean todos
    ) {
        // Monta o mapa de filtros
        Map<String, Object> filtros = new HashMap<>();

        if (id != null) filtros.put("id", id);
        if (espacoId != null) filtros.put("espacoId", espacoId);
        if (secretariaId != null) filtros.put("secretariaId", secretariaId);
        if (Boolean.TRUE.equals(todos)) filtros.put("todos", true);

        // Cria a especificação com base nos filtros
        var spec = SecretariaEspacoSpecification.byFilters(filtros);

        // Executa a consulta e mapeia para o DTO de retorno
        return repository.findAll(spec, pageable).map(SecretariaEspacoRetornoDTO::new);
    }
}
