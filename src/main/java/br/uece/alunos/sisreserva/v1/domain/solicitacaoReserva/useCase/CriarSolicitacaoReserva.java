package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
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
import br.uece.alunos.sisreserva.v1.infra.utils.mail.ReservaEmailService;
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
    @Autowired
    private ReservaEmailService reservaEmailService;

    /**
     * Cria uma ou mais solicitações de reserva baseado nos dados fornecidos.
     * 
     * <p>Se o tipo de recorrência for diferente de NAO_REPETE, cria múltiplas
     * reservas para cada ocorrência calculada. Caso contrário, cria apenas uma reserva.</p>
     * 
     * <p>Suporta tanto reservas de espaço quanto reservas de equipamento.</p>
     * 
     * @param data dados da solicitação de reserva
     * @return DTO com os dados da primeira reserva criada (reserva pai)
     * @throws IllegalArgumentException se houver conflito de horários ou dados inválidos
     */
    public SolicitacaoReservaRetornoDTO criarSolicitacaoReserva(SolicitacaoReservaDTO data) {
        // Validar que foi informado ou espaço ou equipamento (mutuamente exclusivo)
        validator.validarTipoReserva(data.espacoId(), data.equipamentoId());
        
        // Determinar se é reserva de espaço ou equipamento
        boolean isReservaEspaco = data.espacoId() != null && !data.espacoId().isBlank();
        
        // Validar datas da reserva (não pode ser no passado e data fim deve ser posterior à data início)
        validator.validarDatasReserva(data.dataInicio(), data.dataFim());
        
        // Validações específicas por tipo de reserva
        if (isReservaEspaco) {
            // Validar permissão de usuário externo para reservar o espaço
            validator.validarPermissaoUsuarioExterno(data.espacoId());
        } else {
            // Validar que equipamento está vinculado a um espaço
            validator.validarEquipamentoVinculadoAEspaco(data.equipamentoId());
            
            // Validar permissão de usuário externo para reservar o equipamento
            validator.validarPermissaoUsuarioExternoEquipamento(data.equipamentoId());
        }
        
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
        // Determinar se é reserva de espaço ou equipamento
        boolean isReservaEspaco = data.espacoId() != null && !data.espacoId().isBlank();
        
        // Validar se usuário já possui solicitação para o mesmo período
        validator.validarSolicitacaoDuplicada(data.usuarioSolicitanteId(), data.dataInicio(), data.dataFim());
        
        // Validação de conflito de reserva
        if (isReservaEspaco) {
            validator.validarConflitoReserva(data.espacoId(), data.dataInicio(), data.dataFim());
        } else {
            validator.validarConflitoReservaEquipamento(data.equipamentoId(), data.dataInicio(), data.dataFim());
        }

        var solicitacao = obterSolicitacaoComEntidadesRelacionadas(data, TipoRecorrencia.NAO_REPETE, null);

        var solicitacaoSalva = repository.save(solicitacao);

        // Recarregar com relações para enviar notificação (evitar LazyInitializationException)
        var solicitacaoComRelacoes = repository.findByIdWithRelations(solicitacaoSalva.getId())
                .orElse(solicitacaoSalva);
        
        // Enviar notificação para gestores do espaço
        reservaEmailService.notificarGestoresSobreNovaSolicitacao(solicitacaoComRelacoes);

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
        
        // Determinar se é reserva de espaço ou equipamento
        boolean isReservaEspaco = data.espacoId() != null && !data.espacoId().isBlank();
        String targetId = isReservaEspaco ? data.espacoId() : data.equipamentoId();
        
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
        validarTodasOcorrencias(datasOcorrencias, duracaoMinutos, targetId, isReservaEspaco, data.usuarioSolicitanteId());
        
        // Obter entidades relacionadas uma única vez
        Espaco espaco = null;
        Equipamento equipamento = null;
        
        if (isReservaEspaco) {
            espaco = entityHandlerService.obterEspacoPorId(data.espacoId());
        } else {
            equipamento = entityHandlerService.obterEquipamentoPorId(data.equipamentoId());
        }
        
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
            equipamento,
            usuario,
            projeto,
            tipoRecorrencia,
            data.dataFimRecorrencia(),
            null // Reserva pai não tem pai
        );
        
        SolicitacaoReserva reservaPaiSalva = repository.save(reservaPai);
        
        // Recarregar com relações para enviar notificação (evitar LazyInitializationException)
        var reservaPaiComRelacoes = repository.findByIdWithRelations(reservaPaiSalva.getId())
                .orElse(reservaPaiSalva);
        
        // Enviar notificação para gestores do espaço sobre a reserva pai
        reservaEmailService.notificarGestoresSobreNovaSolicitacao(reservaPaiComRelacoes);
        
        // Criar reservas filhas (demais ocorrências)
        if (datasOcorrencias.size() > 1) {
            List<SolicitacaoReserva> reservasFilhas = new ArrayList<>();
            
            for (int i = 1; i < datasOcorrencias.size(); i++) {
                SolicitacaoReserva reservaFilha = criarReserva(
                    datasOcorrencias.get(i),
                    duracaoMinutos,
                    espaco,
                    equipamento,
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
     * @param targetId identificador do espaço ou equipamento
     * @param isReservaEspaco true se for reserva de espaço, false se for de equipamento
     * @throws IllegalArgumentException se houver conflito em alguma data
     */
    private void validarTodasOcorrencias(
            List<LocalDateTime> datasOcorrencias, 
            long duracaoMinutos, 
            String targetId,
            boolean isReservaEspaco,
            String usuarioSolicitanteId) {
        
        for (LocalDateTime dataInicio : datasOcorrencias) {
            LocalDateTime dataFim = dataInicio.plusMinutes(duracaoMinutos);
            
            // Validar se usuário já possui solicitação para o mesmo período
            validator.validarSolicitacaoDuplicada(usuarioSolicitanteId, dataInicio, dataFim);
            
            if (isReservaEspaco) {
                validator.validarConflitoReserva(targetId, dataInicio, dataFim);
            } else {
                validator.validarConflitoReservaEquipamento(targetId, dataInicio, dataFim);
            }
        }
    }

    /**
     * Cria uma instância de SolicitacaoReserva com os dados fornecidos.
     * 
     * @param dataInicio data e hora de início
     * @param duracaoMinutos duração em minutos
     * @param espaco espaço reservado (null se for reserva de equipamento)
     * @param equipamento equipamento reservado (null se for reserva de espaço)
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
            Equipamento equipamento,
            Usuario usuario,
            Projeto projeto,
            TipoRecorrencia tipoRecorrencia,
            LocalDateTime dataFimRecorrencia,
            String reservaPaiId) {
        
        SolicitacaoReserva solicitacao = new SolicitacaoReserva();
        solicitacao.setDataInicio(dataInicio);
        solicitacao.setDataFim(dataInicio.plusMinutes(duracaoMinutos));
        solicitacao.setEspaco(espaco);
        solicitacao.setEquipamento(equipamento);
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
        
        // Determinar se é reserva de espaço ou equipamento
        boolean isReservaEspaco = data.espacoId() != null && !data.espacoId().isBlank();
        
        Espaco espaco = null;
        Equipamento equipamento = null;
        
        if (isReservaEspaco) {
            espaco = entityHandlerService.obterEspacoPorId(data.espacoId());
        } else {
            equipamento = entityHandlerService.obterEquipamentoPorId(data.equipamentoId());
        }
        
        Usuario usuario = entityHandlerService.obterUsuarioPorId(data.usuarioSolicitanteId());
        Projeto projeto = null;
        if (data.projetoId() != null && !data.projetoId().isBlank()) {
            projeto = entityHandlerService.obterProjetoPorId(data.projetoId());
        }

        return fromDTO(data, espaco, equipamento, usuario, projeto, tipoRecorrencia, reservaPaiId);
    }

    /**
     * Converte um DTO em uma entidade SolicitacaoReserva.
     * 
     * @param dto dados da solicitação
     * @param espaco espaço reservado (null se for reserva de equipamento)
     * @param equipamento equipamento reservado (null se for reserva de espaço)
     * @param usuario usuário solicitante
     * @param projeto projeto associado (opcional)
     * @param tipoRecorrencia tipo de recorrência
     * @param reservaPaiId ID da reserva pai (opcional)
     * @return instância de SolicitacaoReserva
     */
    public static SolicitacaoReserva fromDTO(
            SolicitacaoReservaDTO dto, 
            Espaco espaco,
            Equipamento equipamento,
            Usuario usuario, 
            Projeto projeto,
            TipoRecorrencia tipoRecorrencia,
            String reservaPaiId) {
        
        SolicitacaoReserva solicitacao = new SolicitacaoReserva();
        solicitacao.setDataInicio(dto.dataInicio());
        solicitacao.setDataFim(dto.dataFim());
        solicitacao.setEspaco(espaco);
        solicitacao.setEquipamento(equipamento);
        solicitacao.setUsuarioSolicitante(usuario);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setProjeto(projeto);
        solicitacao.setTipoRecorrencia(tipoRecorrencia);
        solicitacao.setDataFimRecorrencia(dto.dataFimRecorrencia());
        solicitacao.setReservaPaiId(reservaPaiId);
        
        return solicitacao;
    }
}
