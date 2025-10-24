package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.TipoRecorrencia;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para processar e gerar datas recorrentes de reservas.
 * 
 * <p>Esta classe é responsável por calcular todas as datas de ocorrência
 * de uma reserva recorrente baseado no tipo de recorrência e período especificado.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
public class RecorrenciaProcessor {

    /**
     * Limite máximo de ocorrências para evitar loops infinitos ou processamento excessivo.
     */
    private static final int MAX_OCORRENCIAS = 365;

    /**
     * Gera uma lista de datas de ocorrência baseado no tipo de recorrência.
     * 
     * @param dataInicio data e hora de início da primeira reserva
     * @param dataFimRecorrencia data até quando as recorrências devem ser geradas
     * @param tipoRecorrencia tipo de recorrência (DIARIA, SEMANAL, MENSAL)
     * @return lista de LocalDateTime representando cada ocorrência
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public static List<LocalDateTime> gerarDatasDasOcorrencias(
            LocalDateTime dataInicio, 
            LocalDateTime dataFimRecorrencia, 
            TipoRecorrencia tipoRecorrencia) {
        
        validarParametros(dataInicio, dataFimRecorrencia, tipoRecorrencia);
        
        List<LocalDateTime> datas = new ArrayList<>();
        datas.add(dataInicio); // Sempre incluir a primeira ocorrência
        
        if (tipoRecorrencia == TipoRecorrencia.NAO_REPETE) {
            return datas;
        }
        
        LocalDateTime dataAtual = dataInicio;
        int contador = 0;
        
        while (contador < MAX_OCORRENCIAS) {
            dataAtual = calcularProximaOcorrencia(dataAtual, tipoRecorrencia);
            
            // Para quando ultrapassar a data fim de recorrência
            if (dataAtual.isAfter(dataFimRecorrencia)) {
                break;
            }
            
            datas.add(dataAtual);
            contador++;
        }
        
        return datas;
    }

    /**
     * Calcula a próxima ocorrência baseado no tipo de recorrência.
     * 
     * @param dataAtual data atual da ocorrência
     * @param tipoRecorrencia tipo de recorrência
     * @return LocalDateTime da próxima ocorrência
     */
    private static LocalDateTime calcularProximaOcorrencia(LocalDateTime dataAtual, TipoRecorrencia tipoRecorrencia) {
        return switch (tipoRecorrencia) {
            case DIARIA -> dataAtual.plusDays(1);
            case SEMANAL -> dataAtual.plusWeeks(1);
            case MENSAL -> calcularProximaOcorrenciaMensal(dataAtual);
            default -> dataAtual;
        };
    }

    /**
     * Calcula a próxima ocorrência mensal, tratando casos especiais de dias do mês.
     * 
     * <p>Quando o dia do mês não existe no mês seguinte (ex: 31 de janeiro -> fevereiro),
     * ajusta para o último dia do mês seguinte.</p>
     * 
     * @param dataAtual data atual da ocorrência
     * @return LocalDateTime da próxima ocorrência mensal
     */
    private static LocalDateTime calcularProximaOcorrenciaMensal(LocalDateTime dataAtual) {
        int diaDoMes = dataAtual.getDayOfMonth();
        LocalDateTime proximoMes = dataAtual.plusMonths(1);
        
        // Verifica se o dia existe no próximo mês
        int ultimoDiaProximoMes = proximoMes.toLocalDate().lengthOfMonth();
        
        if (diaDoMes > ultimoDiaProximoMes) {
            // Se o dia não existe, usa o último dia do mês
            return proximoMes.withDayOfMonth(ultimoDiaProximoMes);
        }
        
        return proximoMes;
    }

    /**
     * Valida os parâmetros de entrada para geração de recorrências.
     * 
     * @param dataInicio data de início
     * @param dataFimRecorrencia data fim de recorrência
     * @param tipoRecorrencia tipo de recorrência
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    private static void validarParametros(
            LocalDateTime dataInicio, 
            LocalDateTime dataFimRecorrencia, 
            TipoRecorrencia tipoRecorrencia) {
        
        if (dataInicio == null) {
            throw new IllegalArgumentException("Data de início não pode ser nula");
        }
        
        if (tipoRecorrencia == null) {
            throw new IllegalArgumentException("Tipo de recorrência não pode ser nulo");
        }
        
        if (tipoRecorrencia != TipoRecorrencia.NAO_REPETE) {
            if (dataFimRecorrencia == null) {
                throw new IllegalArgumentException(
                    "Data fim de recorrência é obrigatória quando o tipo de recorrência não é 'NÃO_REPETE'"
                );
            }
            
            if (dataFimRecorrencia.isBefore(dataInicio)) {
                throw new IllegalArgumentException(
                    "Data fim de recorrência deve ser posterior à data de início"
                );
            }
        }
    }

    /**
     * Calcula a duração da reserva em minutos.
     * 
     * @param dataInicio data e hora de início
     * @param dataFim data e hora de fim
     * @return duração em minutos
     */
    public static long calcularDuracaoEmMinutos(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return ChronoUnit.MINUTES.between(dataInicio, dataFim);
    }

    /**
     * Verifica se uma data está dentro de um período.
     * 
     * @param data data a ser verificada
     * @param periodoInicio início do período
     * @param periodoFim fim do período
     * @return true se a data está dentro do período
     */
    public static boolean estaDentroDoPeriodo(LocalDateTime data, LocalDateTime periodoInicio, LocalDateTime periodoFim) {
        return !data.isBefore(periodoInicio) && !data.isAfter(periodoFim);
    }
}
