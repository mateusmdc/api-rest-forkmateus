package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrencia;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrenciaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation.AtualizarStatusValidator;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.ExcecaoRecorrenciaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.OcorrenciaReservaDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Caso de uso para criar ou atualizar uma exceção em uma ocorrência específica de uma série recorrente.
 *
 * Permite:
 * 
 *   - Cancelar uma ocorrência individual de uma série
 *   - Reagendar (alterar horário) de uma ocorrência individual
 *   - Confirmar / reativar uma ocorrência previamente cancelada
 *
 * Apenas séries com {@code isSerie = true} suportam exceções por ocorrência.
 * Reservas avulsas ou legadas devem ser atualizadas diretamente via
 * {@link AtualizarStatusSolicitacao}.
 *
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
@Slf4j
@Component
public class AtualizarOcorrenciaReserva {

    @Autowired
    private SolicitacaoReservaRepository reservaRepository;

    @Autowired
    private ExcecaoRecorrenciaRepository excecaoRepository;

    @Autowired
    private AtualizarStatusValidator validator;

    /**
     * Aplica uma exceção a uma ocorrência específica de uma série recorrente.
     *
     * @param serieId ID da série recorrente (registro com {@code isSerie = true})
     * @param dto     dados da exceção a ser aplicada
     * @return DTO representando a ocorrência atualizada
     * @throws ValidationException se a série não existir, não for uma série, ou a data não corresponder a uma ocorrência válida
     */
    public OcorrenciaReservaDTO executar(String serieId, ExcecaoRecorrenciaDTO dto) {
        // 1. Carregar e validar a série
        SolicitacaoReserva serie = reservaRepository.findById(serieId)
                .orElseThrow(() -> new ValidationException("Série de reservas não encontrada com ID: " + serieId));

        if (!Boolean.TRUE.equals(serie.getIsSerie())) {
            throw new ValidationException(
                    "Esta solicitação não é uma série recorrente. Use o endpoint de atualização de status para reservas avulsas.");
        }

        // 2. Validar permissão do usuário autenticado
        validator.validarPermissaoParaAtualizarStatus(serie, dto.novoStatus());

        // 3. Verificar se a data informada é uma ocorrência válida da série
        LocalDate dataOcorrencia = dto.dataOcorrencia();
        if (!isOcorrenciaValida(serie, dataOcorrencia)) {
            throw new ValidationException(
                    "A data " + dataOcorrencia + " não corresponde a uma ocorrência válida desta série recorrente.");
        }

        // 4. Criar ou atualizar a exceção
        ExcecaoRecorrencia excecao = excecaoRepository
                .findBySerieIdAndData(serieId, dataOcorrencia)
                .orElseGet(ExcecaoRecorrencia::new);

        excecao.setSolicitacaoReservaId(serieId);
        excecao.setDataOcorrencia(dataOcorrencia);
        excecao.setStatus(dto.novoStatus());
        excecao.setMotivo(dto.motivo());

        LocalDateTime dataInicioNova = dto.dataInicioNova();
        LocalDateTime dataFimNova = dto.dataFimNova();

        // Validação de coerência dos novos horários, se ambos forem informados
        if (dataInicioNova != null && dataFimNova != null) {
            if (!dataFimNova.isAfter(dataInicioNova)) {
                throw new ValidationException(
                        "A data/hora de término da ocorrência deve ser posterior à data/hora de início.");
            }

            LocalDate dataInicioNovaDia = dataInicioNova.toLocalDate();
            LocalDate dataFimNovaDia = dataFimNova.toLocalDate();
            if (!dataInicioNovaDia.equals(dataOcorrencia) || !dataFimNovaDia.equals(dataOcorrencia)) {
                throw new ValidationException(
                        "Os horários informados para a ocorrência devem estar na mesma data da ocorrência (" + dataOcorrencia + ").");
            }
        }

        // Sempre aplicar os valores (inclusive null) para permitir remover overrides anteriores
        excecao.setDataInicioNova(dataInicioNova);
        excecao.setDataFimNova(dataFimNova);

        // Validar conflito de horário quando os horários são alterados e o status é APROVADO
        if (dto.novoStatus() == StatusSolicitacao.APROVADO
                && (dataInicioNova != null || dataFimNova != null)
                && serie.getEspaco() != null) {
            long duracaoMinutosConf = RecorrenciaProcessor.calcularDuracaoEmMinutos(
                    serie.getDataInicio(), serie.getDataFim());
            LocalDateTime inicioVerif = dataInicioNova != null
                    ? dataInicioNova
                    : dataOcorrencia.atTime(serie.getDataInicio().toLocalTime());
            LocalDateTime fimVerif = dataFimNova != null
                    ? dataFimNova
                    : inicioVerif.plusMinutes(duracaoMinutosConf);
            if (reservaRepository.existsConflitoPorEspacoExcluindoId(
                    serie.getEspaco().getId(), inicioVerif, fimVerif, serieId)) {
                throw new ValidationException(
                        "Existe conflito de horário com outra reserva aprovada neste espaço para o período informado.");
            }
        }

        ExcecaoRecorrencia salva = excecaoRepository.save(excecao);

        log.info("[OCORRENCIA] Exceção aplicada na série {} para data {}: status={}, motivo={}",
                serieId, dataOcorrencia, dto.novoStatus(), dto.motivo());

        // 4. Montar retorno
        long duracaoMinutos = RecorrenciaProcessor.calcularDuracaoEmMinutos(serie.getDataInicio(), serie.getDataFim());
        LocalDateTime inicioEfetivo = salva.getDataInicioNova() != null
                ? salva.getDataInicioNova()
                : dataOcorrencia.atTime(serie.getDataInicio().toLocalTime());
        LocalDateTime fimEfetivo = salva.getDataFimNova() != null
                ? salva.getDataFimNova()
                : inicioEfetivo.plusMinutes(duracaoMinutos);

        return new OcorrenciaReservaDTO(
                serieId,
                dataOcorrencia,
                inicioEfetivo,
                fimEfetivo,
                salva.getStatus().getCodigo(),
                true,
                salva.getId(),
                salva.getMotivo()
        );
    }

    /**
     * Busca todas as ocorrências da série e verifica se {@code dataOcorrencia} é uma delas.
     */
    private boolean isOcorrenciaValida(SolicitacaoReserva serie, LocalDate dataOcorrencia) {
        List<LocalDateTime> ocorrencias = RecorrenciaProcessor.gerarDatasDasOcorrencias(
                serie.getDataInicio(), serie.getDataFimRecorrencia(), serie.getTipoRecorrencia());
        return ocorrencias.stream()
                .anyMatch(dt -> dt.toLocalDate().equals(dataOcorrencia));
    }
}
