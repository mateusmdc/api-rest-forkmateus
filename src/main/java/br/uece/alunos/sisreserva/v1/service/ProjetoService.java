package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoDTO;
import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface ProjetoService {
    ProjetoRetornoDTO criarProjeto(ProjetoDTO data);
    Page<ProjetoRetornoDTO> obterProjeto(
        Pageable pageable,
        String id,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        String usuarioResponsavelId,
        String instituicaoId
    
    );
}
