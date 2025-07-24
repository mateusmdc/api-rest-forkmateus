package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntSolicitacaoReservaPorId {
    @Autowired
    private SolicitacaoReservaRepository repository;

    public SolicitacaoReserva obterEntidade(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new ValidationException(
                "Não foi encontrada solicitação de reserva com o ID informado."
            ));
    }
}
