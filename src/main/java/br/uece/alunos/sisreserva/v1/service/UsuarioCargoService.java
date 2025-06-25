package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.UsuarioCargoRetornoDTO;

import java.util.List;

public interface UsuarioCargoService {
    List<UsuarioCargoRetornoDTO> criarEmCargaUsuarioCargo(CriarCargaUsuarioCargoDTO data);
}
