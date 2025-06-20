package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.DTO.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.DTO.UsuarioCargoRetornoDTO;

import java.util.List;

public interface UsuarioCargoService {
    List<UsuarioCargoRetornoDTO> criarEmCargaUsuarioCargo(CriarCargaUsuarioCargoDTO data);
}
