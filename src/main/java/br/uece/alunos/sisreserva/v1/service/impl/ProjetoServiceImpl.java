package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.projeto.UseCase.CriarProjeto;
import br.uece.alunos.sisreserva.v1.domain.projeto.UseCase.ObterProjeto;
import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoDTO;
import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.ProjetoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjetoServiceImpl implements ProjetoService{
    private final CriarProjeto criarProjeto;
    private final ObterProjeto obterProjeto;

    @Override
    public ProjetoRetornoDTO criarProjeto(ProjetoDTO data) {
        return criarProjeto.criarProjeto(data);
    }

    @Override
    public Page<ProjetoRetornoDTO> obterProjeto(Pageable pageable, String id, String nome, String descricao, LocalDate dataInicio, LocalDate dataFim, String usuarioResponsavelId, String instituicaoId) {
        return obterProjeto.obterProjetos(pageable, id, nome, descricao, dataInicio, dataFim, usuarioResponsavelId, instituicaoId);
    }
}
