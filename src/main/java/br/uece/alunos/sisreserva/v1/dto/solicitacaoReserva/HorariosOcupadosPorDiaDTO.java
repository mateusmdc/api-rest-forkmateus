package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.time.LocalDate;
import java.util.List;

public record HorariosOcupadosPorDiaDTO(
    LocalDate data,
    List<HorarioOcupadoDTO> horariosOcupados
) {}