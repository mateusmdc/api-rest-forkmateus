package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrencia;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrenciaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.TipoRecorrencia;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.OcorrenciaReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.RecorrenciaInfoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Caso de uso para obter informações completas sobre uma série de reservas recorrentes.
 *
 * As ocorrências são calculadas dinamicamente a partir das regras de recorrência
 * armazenadas no registro da série. As exceções registradas em {@code excecao_recorrencia}
 * são aplicadas sobre as ocorrências calculadas antes de retornar o resultado.
 *
 * @author Sistema de Reservas UECE
 * @version 2.0
 */
@Component
public class ObterRecorrenciaInfo {

    @Autowired
    private SolicitacaoReservaRepository repository;

    @Autowired
    private ExcecaoRecorrenciaRepository excecaoRepository;

    /**
     * Obtém a série recorrente e calcula todas as suas ocorrências.
     *
     * @param reservaId ID da série recorrente
     * @return informações completas incluindo dados da série e todas as ocorrências calculadas
     * @throws IllegalArgumentException se a reserva não existir ou não for uma série recorrente
     */
    public RecorrenciaInfoDTO obterRecorrenciaInfo(String reservaId) {
        SolicitacaoReserva reserva = repository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada: " + reservaId));

        if (!Boolean.TRUE.equals(reserva.getIsSerie())) {
            throw new IllegalArgumentException(
                    "A reserva ID " + reservaId + " não é uma série recorrente.");
        }

        return obterInfoSerie(reserva);
    }

    // =========================================================================
    // Série única com ocorrências calculadas dinamicamente
    // =========================================================================

    /**
     * Calcula todas as ocorrências da série dinamicamente e aplica as exceções registradas.
     *
     * @param serie registro da série
     * @return informações da série com ocorrências calculadas
     */
    private RecorrenciaInfoDTO obterInfoSerie(SolicitacaoReserva serie) {
        if (serie.getTipoRecorrencia() == TipoRecorrencia.NAO_REPETE) {
            throw new IllegalArgumentException(
                    "A reserva ID " + serie.getId() + " não possui recorrência configurada.");
        }

        // Calcular todas as datas de ocorrência a partir das regras da série
        List<LocalDateTime> datasOcorrencias = RecorrenciaProcessor.gerarDatasDasOcorrencias(
                serie.getDataInicio(),
                serie.getDataFimRecorrencia(),
                serie.getTipoRecorrencia()
        );

        long duracaoMinutos = RecorrenciaProcessor.calcularDuracaoEmMinutos(
                serie.getDataInicio(), serie.getDataFim());

        // Carregar todas as exceções da série em uma única consulta
        List<ExcecaoRecorrencia> excecoes = excecaoRepository.findBySerieId(serie.getId());
        Map<LocalDate, ExcecaoRecorrencia> excecoesPorData = excecoes.stream()
                .collect(Collectors.toMap(ExcecaoRecorrencia::getDataOcorrencia, e -> e));

        // Construir DTOs de ocorrência aplicando exceções
        List<OcorrenciaReservaDTO> ocorrencias = datasOcorrencias.stream()
                .map(dataOcorrencia -> construirOcorrenciaDTO(
                        serie, dataOcorrencia, duracaoMinutos, excecoesPorData))
                .collect(Collectors.toList());

        return new RecorrenciaInfoDTO(new SolicitacaoReservaRetornoDTO(serie), ocorrencias);
    }

    /**
     * Constrói o DTO de uma ocorrência individual, aplicando exceção se existir.
     *
     * @param serie           série de reservas
     * @param dataOcorrencia  data/hora calculada desta ocorrência
     * @param duracaoMinutos  duração padrão da série em minutos
     * @param excecoesPorData mapa de exceções indexadas pela data da ocorrência
     * @return DTO da ocorrência com valores efetivos
     */
    private OcorrenciaReservaDTO construirOcorrenciaDTO(
            SolicitacaoReserva serie,
            LocalDateTime dataOcorrencia,
            long duracaoMinutos,
            Map<LocalDate, ExcecaoRecorrencia> excecoesPorData) {

        LocalDate dataKey = dataOcorrencia.toLocalDate();
        ExcecaoRecorrencia excecao = excecoesPorData.get(dataKey);

        // Determinar horários e status efetivos (da exceção se existir, senão da série)
        LocalDateTime dataInicioEfetiva = excecao != null && excecao.getDataInicioNova() != null
                ? excecao.getDataInicioNova()
                : dataOcorrencia;

        LocalDateTime dataFimEfetiva = excecao != null && excecao.getDataFimNova() != null
                ? excecao.getDataFimNova()
                : dataInicioEfetiva.plusMinutes(duracaoMinutos);

        Integer statusEfetivo = excecao != null
                ? excecao.getStatus().getCodigo()
                : serie.getStatus().getCodigo();

        return new OcorrenciaReservaDTO(
                serie.getId(),
                dataKey,
                dataInicioEfetiva,
                dataFimEfetiva,
                statusEfetivo,
                excecao != null,
                excecao != null ? excecao.getId() : null,
                excecao != null ? excecao.getMotivo() : null
        );
    }

    /**
     * Obtém apenas as ocorrências (sem o registro da série) de uma série pelo ID.
     * Útil para operações que precisam apenas da lista de datas.
     *
     * @param serieId ID da série (deve ser um registro com {@code isSerie = true})
     * @return lista de ocorrências calculadas
     */
    public List<OcorrenciaReservaDTO> obterOcorrenciasDaSerie(String serieId) {
        RecorrenciaInfoDTO info = obterRecorrenciaInfo(serieId);
        return info.ocorrencias();
    }

    /**
     * Encontra uma ocorrência específica dentro de uma série pelo ID e data.
     *
     * @param serieId         ID da série
     * @param dataOcorrencia  data da ocorrência desejada
     * @return a ocorrência com os valores efetivos, se existir
     */
    public Optional<OcorrenciaReservaDTO> obterOcorrenciaPorData(String serieId, LocalDate dataOcorrencia) {
        return obterOcorrenciasDaSerie(serieId).stream()
                .filter(o -> o.dataOcorrencia().equals(dataOcorrencia))
                .findFirst();
    }
}
