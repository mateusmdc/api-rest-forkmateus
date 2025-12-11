package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.specification.UsuarioSpecification;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ObterUsuarios {

    private final UsuarioRepository repository;
    private final UtilsService utilsService;

    public Page<UsuarioRetornoDTO> obter(Pageable pageable,
                                         String id,
                                         String matricula,
                                         String email,
                                         String documentoFiscal,
                                         String instituicaoId,
                                         String cargoId,
                                         String nome) {

        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (matricula != null) filtros.put("matricula", matricula);
        if (email != null) filtros.put("email", email);
        if (documentoFiscal != null) filtros.put("documentoFiscal", documentoFiscal);
        if (instituicaoId != null) filtros.put("instituicaoId", instituicaoId);
        if (cargoId != null) filtros.put("cargoId", cargoId);

        var spec = UsuarioSpecification.byFilters(filtros);
        var resultados = repository.findAll(spec, Sort.unsorted());

        if (nome != null && !nome.isBlank()) {
            String nomeNormalizado = utilsService.normalizeString(nome);
            resultados = resultados.stream()
                    .filter(u -> utilsService.normalizeString(u.getNome()).contains(nomeNormalizado))
                    .toList();
        }

        int total = resultados.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<UsuarioRetornoDTO> page = resultados.subList(start, end).stream()
                .map(UsuarioRetornoDTO::new)
                .toList();

        return new PageImpl<>(page, pageable, total);
    }
}