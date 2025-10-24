package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.TipoRecorrencia;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation.SolicitacaoReservaValidator;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso para criação de solicitações de reserva.
 * 
 * <p>Suporta tanto reservas únicas quanto reservas recorrentes.
 * Para reservas recorrentes, cria automaticamente todas as ocorrências
 * baseadas no tipo de recorrência e período especificado.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
@Component
public class CriarSolicitacaoReserva {
    @Autowired
    private EntityHandlerService entityHandlerService;
    @Autowired
    private SolicitacaoReservaValidator validator;
    @Autowired
    private SolicitacaoReservaRepository repository;

    /**
     * Cria uma ou mais solicitações de reserva baseado nos dados fornecidos.
     * 
     * <p>Se o tipo de recorrência for diferente de NAO_REPETE, cria múltiplas
     * reservas para cada ocorrência calculada. Caso contrário, cria apenas uma reserva.</p>
     * 
     * @param data dados da solicitação de reserva
     * @return DTO com os dados da primeira reserva criada (reserva pai)
     * @throws IllegalArgumentException se houver conflito de horários ou dados inválidos
     */
    public SolicitacaoReservaRetornoDTO criarSolicitacaoReserva(SolicitacaoReservaDTO data) {
        // Validar datas da reserva (não pode ser no passado e data fim deve ser posterior à data início)
        validator.validarDatasReserva(data.dataInicio(), data.dataFim());
        
        // Determinar tipo de recorrência (default: NAO_REPETE)
        TipoRecorrencia tipoRecorrencia = data.tipoRecorrencia() != null 
            ? TipoRecorrencia.fromCodigo(data.tipoRecorrencia()) 
            : TipoRecorrencia.NAO_REPETE;
        
        // Validar dados de recorrência
        validator.validarDadosRecorrencia(tipoRecorrencia, data.dataFimRecorrencia());
        
        // Se não há recorrência, criar reserva única
        if (tipoRecorrencia == TipoRecorrencia.NAO_REPETE) {
            return criarReservaUnica(data);
        }
        
        // Criar reservas recorrentes
        return criarReservasRecorrentes(data, tipoRecorrencia);
    }

    /**
     * Cria uma reserva única sem recorrência.
     * 
     * @param data dados da reserva
     * @return DTO com os dados da reserva criada
     */
    private SolicitacaoReservaRetornoDTO criarReservaUnica(SolicitacaoReservaDTO data) {
        // Validação de conflito de reserva
        validator.validarConflitoReserva(data.espacoId(), data.dataInicio(), data.dataFim());

        var solicitacao = obterSolicitacaoComEntidadesRelacionadas(data, TipoRecorrencia.NAO_REPETE, null);

        var solicitacaoSalva = repository.save(solicitacao);

        return new SolicitacaoReservaRetornoDTO(solicitacaoSalva);
    }

    /**
     * Cria múltiplas reservas recorrentes baseado no tipo de recorrência.
     * 
     * <p>Gera todas as datas de ocorrência, valida cada uma para conflitos,
     * e cria as reservas em batch. A primeira reserva é a "reserva pai" e as
     * demais referenciam ela através do campo reservaPaiId.</p>
     * 
     * @param data dados da reserva
     * @param tipoRecorrencia tipo de recorrência
     * @return DTO com os dados da reserva pai criada
     * @throws IllegalArgumentException se houver conflito em alguma das datas
     */
    private SolicitacaoReservaRetornoDTO criarReservasRecorrentes(
            SolicitacaoReservaDTO data, 
            TipoRecorrencia tipoRecorrencia) {
        
        // Gerar todas as datas de ocorrência
        List<LocalDateTime> datasOcorrencias = RecorrenciaProcessor.gerarDatasDasOcorrencias(
            data.dataInicio(), 
            data.dataFimRecorrencia(), 
            tipoRecorrencia
        );
        
        // Calcular duração da reserva
        long duracaoMinutos = RecorrenciaProcessor.calcularDuracaoEmMinutos(
            data.dataInicio(), 
            data.dataFim()
        );
        
        // Validar conflitos para todas as ocorrências
        validarTodasOcorrencias(datasOcorrencias, duracaoMinutos, data.espacoId());
        
        // Obter entidades relacionadas uma única vez
        Espaco espaco = entityHandlerService.obterEspacoPorId(data.espacoId());
        Usuario usuario = entityHandlerService.obterUsuarioPorId(data.usuarioSolicitanteId());
        Projeto projeto = null;
        if (data.projetoId() != null && !data.projetoId().isBlank()) {
            projeto = entityHandlerService.obterProjetoPorId(data.projetoId());
        }
        
        // Criar reserva pai (primeira ocorrência)
        SolicitacaoReserva reservaPai = criarReserva(
            datasOcorrencias.get(0),
            duracaoMinutos,
            espaco,
            usuario,
            projeto,
            tipoRecorrencia,
            data.dataFimRecorrencia(),
            null // Reserva pai não tem pai
        );
        
        SolicitacaoReserva reservaPaiSalva = repository.save(reservaPai);
        
        // Criar reservas filhas (demais ocorrências)
        if (datasOcorrencias.size() > 1) {
            List<SolicitacaoReserva> reservasFilhas = new ArrayList<>();
            
            for (int i = 1; i < datasOcorrencias.size(); i++) {
                SolicitacaoReserva reservaFilha = criarReserva(
                    datasOcorrencias.get(i),
                    duracaoMinutos,
                    espaco,
                    usuario,
                    projeto,
                    tipoRecorrencia,
                    data.dataFimRecorrencia(),
                    reservaPaiSalva.getId()
                );
                reservasFilhas.add(reservaFilha);
            }
            
            // Salvar todas as reservas filhas em batch
            repository.saveAll(reservasFilhas);
        }
        
        return new SolicitacaoReservaRetornoDTO(reservaPaiSalva);
    }

    /**
     * Valida se todas as ocorrências estão livres de conflitos.
     * 
     * @param datasOcorrencias lista de datas das ocorrências
     * @param duracaoMinutos duração de cada reserva em minutos
     * @param espacoId identificador do espaço
     * @throws IllegalArgumentException se houver conflito em alguma data
     */
    private void validarTodasOcorrencias(
            List<LocalDateTime> datasOcorrencias, 
            long duracaoMinutos, 
            String espacoId) {
        
        for (LocalDateTime dataInicio : datasOcorrencias) {
            LocalDateTime dataFim = dataInicio.plusMinutes(duracaoMinutos);
            validator.validarConflitoReserva(espacoId, dataInicio, dataFim);
        }
    }

    /**
     * Cria uma instância de SolicitacaoReserva com os dados fornecidos.
     * 
     * @param dataInicio data e hora de início
     * @param duracaoMinutos duração em minutos
     * @param espaco espaço reservado
     * @param usuario usuário solicitante
     * @param projeto projeto associado (opcional)
     * @param tipoRecorrencia tipo de recorrência
     * @param dataFimRecorrencia data fim de recorrência
     * @param reservaPaiId ID da reserva pai (null se for a reserva pai)
     * @return instância de SolicitacaoReserva
     */
    private SolicitacaoReserva criarReserva(
            LocalDateTime dataInicio,
            long duracaoMinutos,
            Espaco espaco,
            Usuario usuario,
            Projeto projeto,
            TipoRecorrencia tipoRecorrencia,
            LocalDateTime dataFimRecorrencia,
            String reservaPaiId) {
        
        SolicitacaoReserva solicitacao = new SolicitacaoReserva();
        solicitacao.setDataInicio(dataInicio);
        solicitacao.setDataFim(dataInicio.plusMinutes(duracaoMinutos));
        solicitacao.setEspaco(espaco);
        solicitacao.setUsuarioSolicitante(usuario);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setProjeto(projeto);
        solicitacao.setTipoRecorrencia(tipoRecorrencia);
        solicitacao.setDataFimRecorrencia(dataFimRecorrencia);
        solicitacao.setReservaPaiId(reservaPaiId);
        
        return solicitacao;
    }

    /**
     * Obtém uma solicitação de reserva com todas as entidades relacionadas carregadas.
     * 
     * @param data dados da solicitação
     * @param tipoRecorrencia tipo de recorrência
     * @param reservaPaiId ID da reserva pai (opcional)
     * @return instância de SolicitacaoReserva
     */
    public SolicitacaoReserva obterSolicitacaoComEntidadesRelacionadas(
            SolicitacaoReservaDTO data,
            TipoRecorrencia tipoRecorrencia,
            String reservaPaiId) {
        
        Espaco espaco = entityHandlerService.obterEspacoPorId(data.espacoId());
        Usuario usuario = entityHandlerService.obterUsuarioPorId(data.usuarioSolicitanteId());
        Projeto projeto = null;
        if (data.projetoId() != null && !data.projetoId().isBlank()) {
            projeto = entityHandlerService.obterProjetoPorId(data.projetoId());
        }

        return fromDTO(data, espaco, usuario, projeto, tipoRecorrencia, reservaPaiId);
    }

    /**
     * Converte um DTO em uma entidade SolicitacaoReserva.
     * 
     * @param dto dados da solicitação
     * @param espaco espaço reservado
     * @param usuario usuário solicitante
     * @param projeto projeto associado (opcional)
     * @param tipoRecorrencia tipo de recorrência
     * @param reservaPaiId ID da reserva pai (opcional)
     * @return instância de SolicitacaoReserva
     */
    public static SolicitacaoReserva fromDTO(
            SolicitacaoReservaDTO dto, 
            Espaco espaco, 
            Usuario usuario, 
            Projeto projeto,
            TipoRecorrencia tipoRecorrencia,
            String reservaPaiId) {
        
        SolicitacaoReserva solicitacao = new SolicitacaoReserva();
        solicitacao.setDataInicio(dto.dataInicio());
        solicitacao.setDataFim(dto.dataFim());
        solicitacao.setEspaco(espaco);
        solicitacao.setUsuarioSolicitante(usuario);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setProjeto(projeto);
        solicitacao.setTipoRecorrencia(tipoRecorrencia);
        solicitacao.setDataFimRecorrencia(dto.dataFimRecorrencia());
        solicitacao.setReservaPaiId(reservaPaiId);
        
        return solicitacao;
    }
}
