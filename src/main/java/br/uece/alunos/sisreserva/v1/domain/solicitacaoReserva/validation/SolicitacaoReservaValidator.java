package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.TipoRecorrencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Validador de regras de negócio para solicitações de reserva.
 * 
 * <p>Valida conflitos de horários e regras de recorrência.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
@Component
public class SolicitacaoReservaValidator {

    @Autowired
    private SolicitacaoReservaRepository repository;

    /**
     * Valida se já existe uma solicitação de reserva aprovada para o mesmo espaço e período informado.
     * 
     * @param espacoId identificador do espaço
     * @param dataInicio data e hora de início da reserva
     * @param dataFim data e hora de fim da reserva
     * @throws IllegalArgumentException se houver conflito de horários
     */
    public void validarConflitoReserva(String espacoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        boolean existeConflito = repository.existsByEspacoIdAndPeriodoConflitanteAprovado(espacoId, dataInicio, dataFim);
        if (existeConflito) {
            throw new IllegalArgumentException("Já existe uma solicitação de reserva aprovada para este espaço no período informado.");
        }
    }

    /**
     * Valida os dados de recorrência de uma reserva.
     * 
     * <p>Verifica se a data fim de recorrência foi informada quando necessária
     * e se está em um período válido.</p>
     * 
     * @param tipoRecorrencia tipo de recorrência da reserva
     * @param dataFimRecorrencia data até quando a recorrência deve se repetir
     * @throws IllegalArgumentException se os dados de recorrência forem inválidos
     */
    public void validarDadosRecorrencia(TipoRecorrencia tipoRecorrencia, LocalDateTime dataFimRecorrencia) {
        if (tipoRecorrencia == null) {
            throw new IllegalArgumentException("Tipo de recorrência não pode ser nulo");
        }

        // Se é uma reserva recorrente, data fim de recorrência é obrigatória
        if (tipoRecorrencia != TipoRecorrencia.NAO_REPETE) {
            if (dataFimRecorrencia == null) {
                throw new IllegalArgumentException(
                    "Data fim de recorrência é obrigatória para reservas recorrentes. " +
                    "Tipo de recorrência informado: " + tipoRecorrencia.getDescricao()
                );
            }

            // Validar que a data fim de recorrência não está muito distante (máximo 1 ano)
            LocalDateTime maxDataFim = LocalDateTime.now().plusYears(1);
            if (dataFimRecorrencia.isAfter(maxDataFim)) {
                throw new IllegalArgumentException(
                    "Data fim de recorrência não pode ser superior a 1 ano a partir de hoje"
                );
            }
        }
    }
}