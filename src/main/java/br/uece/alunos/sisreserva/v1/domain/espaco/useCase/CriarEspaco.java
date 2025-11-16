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

import java.util.List;

/**
 * Caso de uso para criação de espaço.
 * Responsável por validar e criar um novo espaço com seus relacionamentos.
 */
@Component
public class CriarEspaco {
    @Autowired
    private EntityHandlerService entityHandlerService;
    @Autowired
    private EspacoValidator validator;
    @Autowired
    private EspacoRepository repository;

    /**
     * Cria um novo espaço com base nos dados fornecidos.
     * 
     * @param data DTO contendo os dados do espaço a ser criado
     * @return DTO com os dados do espaço criado
     */
    public EspacoRetornoDTO criarEspaco(EspacoDTO data) {
        // Valida se já existe um espaço com o mesmo nome, departamento e localização
        validator.validarSeEspacoJaExiste(
                data.nome(), data.departamentoId(), data.localizacaoId()
        );

        // Obtém o espaço com todas as entidades relacionadas
        var espaco = obterEspacoComEntidadesRelacionadas(data);

        // Salva o espaço no banco de dados
        var espacoSalvo = repository.save(espaco);

        return new EspacoRetornoDTO(espacoSalvo);
    }

    /**
     * Obtém a entidade Espaco com todas as entidades relacionadas carregadas.
     * 
     * @param data DTO contendo os IDs das entidades relacionadas
     * @return entidade Espaco com relacionamentos carregados
     */
    public Espaco obterEspacoComEntidadesRelacionadas(EspacoDTO data) {
        var departamento = entityHandlerService.obterDepartamentoPorId(data.departamentoId());
        var localizacao = entityHandlerService.obterLocalizacaoPorId(data.localizacaoId());
        var tipoEspaco = entityHandlerService.obterTipoEspacoPorId(data.tipoEspacoId());
        
        // Obtém todos os tipos de atividade pelos IDs fornecidos
        var tiposAtividade = data.tipoAtividadeIds().stream()
                .map(id -> entityHandlerService.obterTipoAtividadePorId(id))
                .toList();

        return fromDTO(data, departamento, localizacao, tipoEspaco, tiposAtividade);
    }

    /**
     * Converte um DTO em entidade Espaco.
     * 
     * @param dto DTO com os dados do espaço
     * @param departamento entidade Departamento relacionada
     * @param localizacao entidade Localizacao relacionada
     * @param tipoEspaco entidade TipoEspaco relacionada
     * @param tiposAtividade lista de entidades TipoAtividade relacionadas
     * @return entidade Espaco preenchida
     */
    public static Espaco fromDTO(EspacoDTO dto, Departamento departamento, Localizacao localizacao,
                                 TipoEspaco tipoEspaco, List<TipoAtividade> tiposAtividade) {
        Espaco espaco = new Espaco();
        espaco.setNome(dto.nome());
        espaco.setUrlCnpq(dto.urlCnpq());
        espaco.setObservacao(dto.observacao());
        espaco.setDepartamento(departamento);
        espaco.setLocalizacao(localizacao);
        espaco.setTipoEspaco(tipoEspaco);
        espaco.getTiposAtividade().addAll(tiposAtividade);
        espaco.setPrecisaProjeto(dto.precisaProjeto());
        espaco.setMultiusuario(dto.multiusuario() != null ? dto.multiusuario() : false);
        return espaco;
    }
}