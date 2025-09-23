package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.util.List;

public record HorariosOcupadosPorMesDTO(
    int mes,
    int ano,
    List<HorariosOcupadosPorDiaDTO> diasComHorariosOcupados
) {}