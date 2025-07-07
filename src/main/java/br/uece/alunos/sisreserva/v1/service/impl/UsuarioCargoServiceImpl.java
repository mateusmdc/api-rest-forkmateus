package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase.*;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.*;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioCargoServiceImpl implements UsuarioCargoService {
    private final ApagarUsuarioCargo apagarUsuarioCargo;
    private final AtualizarUsuarioCargos atualizarUsuarioCargos;
    private final CriarUsuarioCargo criarUsuarioCargo;
    private final CriarEmCargaUsuarioCargo criarEmCargaUsuarioCargo;
    private final ObterCargosUsuarioId obterCargosUsuarioId;

    @Override
    public void atualizarCargos(List<String> cargosId, String idUsuario) {
        atualizarUsuarioCargos.atualizarCargos(cargosId, idUsuario);
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

    @Override
    public void remover(ApagarUsuarioCargoDTO data) {
        apagarUsuarioCargo.remover(data);
    }

}