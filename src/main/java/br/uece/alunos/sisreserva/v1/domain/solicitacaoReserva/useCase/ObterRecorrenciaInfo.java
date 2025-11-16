package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.RecorrenciaInfoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso para obter informações sobre reservas recorrentes.
 * 
 * <p>Permite consultar todas as ocorrências de uma reserva recorrente,
 * incluindo a reserva pai e todas as reservas filhas.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
@Component
public class ObterRecorrenciaInfo {

    @Autowired
    private SolicitacaoReservaRepository repository;

    /**
     * Obtém todas as reservas de um grupo de recorrência.
     * 
     * <p>Se o ID fornecido for de uma reserva pai, retorna ela e todas as filhas.
     * Se for de uma reserva filha, busca a reserva pai e todas as outras filhas.</p>
     * 
     * @param reservaId ID da reserva (pode ser pai ou filha)
     * @return informações completas da recorrência
     * @throws IllegalArgumentException se a reserva não existir
     */
    public RecorrenciaInfoDTO obterRecorrenciaInfo(String reservaId) {
        SolicitacaoReserva reserva = repository.findById(reservaId)
            .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada: " + reservaId));

        // Determinar o ID da reserva pai
        String reservaPaiId = reserva.getReservaPaiId() != null 
            ? reserva.getReservaPaiId() 
            : reserva.getId();

        // Buscar reserva pai e todas as filhas
        List<SolicitacaoReserva> todasReservas = repository.findReservasPaiEFilhas(reservaPaiId);

        if (todasReservas.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma reserva encontrada para o ID: " + reservaPaiId);
        }

        // Separar reserva pai das filhas
        SolicitacaoReserva reservaPai = todasReservas.stream()
            .filter(r -> r.getReservaPaiId() == null)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Reserva pai não encontrada"));

        List<SolicitacaoReservaRetornoDTO> reservasFilhas = todasReservas.stream()
            .filter(r -> r.getReservaPaiId() != null)
            .map(SolicitacaoReservaRetornoDTO::new)
            .collect(Collectors.toList());

        return new RecorrenciaInfoDTO(
            new SolicitacaoReservaRetornoDTO(reservaPai),
            reservasFilhas
        );
    }

    /**
     * Obtém apenas as reservas filhas de uma reserva pai.
     * 
     * @param reservaPaiId ID da reserva pai
     * @return lista de reservas filhas
     */
    public List<SolicitacaoReservaRetornoDTO> obterReservasFilhas(String reservaPaiId) {
        List<SolicitacaoReserva> reservasFilhas = repository.findByReservaPaiId(reservaPaiId);
        return reservasFilhas.stream()
            .map(SolicitacaoReservaRetornoDTO::new)
            .collect(Collectors.toList());
    }

    /**
     * Conta quantas ocorrências existem em um grupo de recorrência.
     * 
     * @param reservaPaiId ID da reserva pai
     * @return número total de ocorrências (incluindo a reserva pai)
     */
    public Long contarOcorrencias(String reservaPaiId) {
        Long countFilhas = repository.countByReservaPaiId(reservaPaiId);
        return countFilhas + 1; // +1 para incluir a reserva pai
    }
}
