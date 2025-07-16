package br.uece.alunos.sisreserva.v1.domain.projeto.UseCase;

import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import br.uece.alunos.sisreserva.v1.domain.projeto.ProjetoRepository;
import br.uece.alunos.sisreserva.v1.domain.projeto.specification.ProjetoSpecification;
import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class ObterProjeto {
    @Autowired
    private ProjetoRepository projetoRepository;
    
    public Page<ProjetoRetornoDTO> obterProjetos(
        Pageable pageable,
        String id,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        String usuarioResponsavelId,
        String instituicaoId
    ){
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (nome != null) filtros.put("nome", nome);
        if (descricao != null) filtros.put("descricao", descricao);
        if (dataInicio != null) filtros.put("dataInicio", dataInicio);
        if (dataFim != null) filtros.put("dataFim", dataFim);
        if (usuarioResponsavelId != null) filtros.put("usuarioResponsavelId", usuarioResponsavelId);
        if (instituicaoId != null) filtros.put("instituicaoId", instituicaoId);

        return execute(filtros, pageable).map(ProjetoRetornoDTO::new);
    }
    
    private Page<Projeto> execute(Map<String, Object> filtros, Pageable pageable) {
        return projetoRepository.findAll(
            ProjetoSpecification.byFilter(
                (String) filtros.get("id"),
                (String) filtros.get("nome"),
                (String) filtros.get("descricao"),
                (LocalDate) filtros.get("dataInicio"),
                (LocalDate) filtros.get("dataFim"),
                (String) filtros.get("usuarioResponsavelId"),
                (String) filtros.get("instituicaoId")
            ),
            pageable
        );
    }
}
