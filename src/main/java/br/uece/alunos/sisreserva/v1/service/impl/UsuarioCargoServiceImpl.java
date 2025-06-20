package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.DTO.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.DTO.UsuarioCargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase.CriarEmCargaUsuarioCargo;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioCargoServiceImpl implements UsuarioCargoService {
    @Autowired
    private CriarEmCargaUsuarioCargo criarEmCargaUsuarioCargo;

    @Override
    public List<UsuarioCargoRetornoDTO> criarEmCargaUsuarioCargo(CriarCargaUsuarioCargoDTO data) {
        return criarEmCargaUsuarioCargo.criarEmCargaUsuarioCargo(data);
    }
}
