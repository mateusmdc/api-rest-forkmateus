package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation.SolicitacaoReservaValidator;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarSolicitacaoReserva {
    @Autowired
    private EntityHandlerService entityHandlerService;
    @Autowired
    private SolicitacaoReservaValidator validator;
    @Autowired
    private SolicitacaoReservaRepository repository;

    public SolicitacaoReservaRetornoDTO criarSolicitacaoReserva(SolicitacaoReservaDTO data) {
        // Validação de conflito de reserva
        validator.validarConflitoReserva(data.espacoId(), data.dataInicio(), data.dataFim());

        var solicitacao = obterSolicitacaoComEntidadesRelacionadas(data);

        var solicitacaoSalva = repository.save(solicitacao);

        return new SolicitacaoReservaRetornoDTO(solicitacaoSalva);
    }

    public SolicitacaoReserva obterSolicitacaoComEntidadesRelacionadas(SolicitacaoReservaDTO data) {
        Espaco espaco = entityHandlerService.obterEspacoPorId(data.espacoId());
        Usuario usuario = entityHandlerService.obterUsuarioPorId(data.usuarioSolicitanteId());
        Projeto projeto = null;
        if (data.projetoId() != null && !data.projetoId().isBlank()) {
            projeto = entityHandlerService.obterProjetoPorId(data.projetoId());
        }

        return fromDTO(data, espaco, usuario, projeto);
    }

    public static SolicitacaoReserva fromDTO(SolicitacaoReservaDTO dto, Espaco espaco, Usuario usuario, Projeto projeto) {
        SolicitacaoReserva solicitacao = new SolicitacaoReserva();
        solicitacao.setDataInicio(dto.dataInicio());
        solicitacao.setDataFim(dto.dataFim());
        solicitacao.setEspaco(espaco);
        solicitacao.setUsuarioSolicitante(usuario);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE); // Sempre pendente ao criar
        solicitacao.setProjeto(projeto);
        return solicitacao;
    }
}
