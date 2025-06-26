package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.CriarUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.UsuarioCargoRetornoDTO;

import java.util.List;

public interface UsuarioCargoService {
    UsuarioCargoRetornoDTO criar(CriarUsuarioCargoDTO data);
    List<UsuarioCargoRetornoDTO> criarEmCargaUsuarioCargo(CriarCargaUsuarioCargoDTO data);
    List<UsuarioCargoRetornoDTO> obterCargosPorIdUsuario(String idUsuario);
}
