package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SolicitacaoReservaValidator {

    @Autowired
    private SolicitacaoReservaRepository repository;

    /**
     * Valida se já existe uma solicitação de reserva para o mesmo espaço e período informado.
     * Lança IllegalArgumentException se houver conflito.
     */
    public void validarConflitoReserva(String espacoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        boolean existeConflito = repository.existsByEspacoIdAndPeriodoConflitanteAprovado(espacoId, dataInicio, dataFim);
        if (existeConflito) {
            throw new IllegalArgumentException("Já existe uma solicitação de reserva para este espaço no período informado.");
        }
    }
}