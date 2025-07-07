package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.validation.EspacoValidator;
import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividade;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarEspaco {
    @Autowired
    private EntityHandlerService entityHandlerService;
    @Autowired
    private EspacoValidator validator;
    @Autowired
    private EspacoRepository repository;

    public EspacoRetornoDTO criarEspaco(EspacoDTO data) {
        validator.validarSeEspacoJaExiste(
                data.nome(), data.departamentoId(), data.localizacaoId()
        );

        var espaco = obterEspacoComEntidadesRelacionadas(data);

        var espacoSalvo = repository.save(espaco);

        return new EspacoRetornoDTO(espacoSalvo);
    }

    public Espaco obterEspacoComEntidadesRelacionadas(EspacoDTO data) {
        var departamento = entityHandlerService.obterDepartamentoPorId(data.departamentoId());
        var localizacao = entityHandlerService.obterLocalizacaoPorId(data.localizacaoId());
        var tipoEspaco = entityHandlerService.obterTipoEspacoPorId(data.tipoEspacoId());
        var tipoAtividade = entityHandlerService.obterTipoAtividadePorId(data.tipoAtividadeId());

        return fromDTO(data, departamento, localizacao, tipoEspaco, tipoAtividade);
    }

    public static Espaco fromDTO(EspacoDTO dto, Departamento departamento, Localizacao localizacao,
                                 TipoEspaco tipoEspaco, TipoAtividade tipoAtividade) {
        Espaco espaco = new Espaco();
        espaco.setNome(dto.nome());
        espaco.setUrlCnpq(dto.urlCnpq());
        espaco.setObservacao(dto.observacao());
        espaco.setDepartamento(departamento);
        espaco.setLocalizacao(localizacao);
        espaco.setTipoEspaco(tipoEspaco);
        espaco.setTipoAtividade(tipoAtividade);
        espaco.setPrecisaProjeto(dto.precisaProjeto());
        return espaco;
    }
}
