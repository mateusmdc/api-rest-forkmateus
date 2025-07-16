package br.uece.alunos.sisreserva.v1.domain.projeto.UseCase;

import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import br.uece.alunos.sisreserva.v1.domain.projeto.ProjetoRepository;
import br.uece.alunos.sisreserva.v1.domain.projeto.validation.ProjetoValidator;
import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoDTO;
import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarProjeto {
    @Autowired
    private EntityHandlerService entityHandlerService;
    @Autowired
    private ProjetoValidator validator;
    @Autowired
    private ProjetoRepository repository;

    public ProjetoRetornoDTO criarProjeto(ProjetoDTO data) {
        validator.validarSeProjetoJaExiste(
            data.nome(), data.usuarioResponsavelId(), data.instituicaoId()
        );

        var projeto = obterProjetoComEntidadesRelacionadas(data);

        var projetoSalvo = repository.save(projeto);

        return new ProjetoRetornoDTO(projetoSalvo);
    }

    public Projeto obterProjetoComEntidadesRelacionadas(ProjetoDTO data) {
        var usuario = entityHandlerService.obterUsuarioPorId(data.usuarioResponsavelId());
        var instituicao = entityHandlerService.obterInstituicaoPorId(data.instituicaoId());
        
        return fromDTO(data, usuario, instituicao);
    }

    public static Projeto fromDTO(ProjetoDTO dto, Usuario usuario, Instituicao instituicao) {
        Projeto projeto = new Projeto();
        projeto.setNome(dto.nome());
        projeto.setDescricao(dto.descricao());
        projeto.setDataInicio(dto.dataInicio());
        projeto.setDataFim(dto.dataFim());
        projeto.setUsuarioResponsavel(usuario);
        projeto.setInstituicao(instituicao);
        return projeto;
    }
        
}
