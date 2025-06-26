package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase;

import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.*;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AtualizarCargos {
    @Autowired
    private UsuarioCargoService usuarioCargoService;

    public void atualizarCargos(List<String> cargosId, String idUsuario) {
        var usuarioCargosNoBanco = usuarioCargoService.obterCargosPorIdUsuario(idUsuario);

        Set<String> cargosIdNoBanco = usuarioCargosNoBanco.stream()
                .map(UsuarioCargoRetornoDTO::cargoId)
                .collect(Collectors.toSet());

        for (String cargoId : cargosId) {
            if (!cargosIdNoBanco.contains(cargoId)) {
                var novoUsuarioCargo = usuarioCargoService.criar(new CriarUsuarioCargoDTO(idUsuario, cargoId));
            }
        }

        for (String cargoIdNoBanco : cargosIdNoBanco) {
            if (!cargosId.contains(cargoIdNoBanco)) {
                usuarioCargoService.remover(new ApagarUsuarioCargoDTO(idUsuario, cargoIdNoBanco));
            }
        }
    }
}
