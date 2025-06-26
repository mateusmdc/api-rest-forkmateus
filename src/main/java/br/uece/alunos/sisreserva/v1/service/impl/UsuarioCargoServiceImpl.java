package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase.*;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.*;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioCargoServiceImpl implements UsuarioCargoService {
    @Autowired
    private ApagarUsuarioCargo apagarUsuarioCargo;
    @Autowired
    private CriarUsuarioCargo criarUsuarioCargo;
    @Autowired
    private CriarEmCargaUsuarioCargo criarEmCargaUsuarioCargo;
    @Autowired
    private ObterCargosUsuarioId obterCargosUsuarioId;

    @Override
    public void remover(ApagarUsuarioCargoDTO data) {
        apagarUsuarioCargo.remover(data);
    }

    @Override
    public UsuarioCargoRetornoDTO criar(CriarUsuarioCargoDTO data) {
        return criarUsuarioCargo.criar(data);
    }

    @Override
    public List<UsuarioCargoRetornoDTO> criarEmCargaUsuarioCargo(CriarCargaUsuarioCargoDTO data) {
        return criarEmCargaUsuarioCargo.criarEmCargaUsuarioCargo(data);
    }

    @Override
    public List<UsuarioCargoRetornoDTO> obterCargosPorIdUsuario(String idUsuario) {
        return obterCargosUsuarioId.obterCargosPorIdUsuario(idUsuario);
    }
}
