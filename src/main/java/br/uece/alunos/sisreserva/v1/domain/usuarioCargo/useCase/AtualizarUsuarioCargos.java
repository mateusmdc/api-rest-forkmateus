package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase;

import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AtualizarUsuarioCargos {
    @Autowired
    private CriarUsuarioCargo criarUsuarioCargo;
    @Autowired
    private ApagarUsuarioCargo apagarUsuarioCargo;
    @Autowired
    private ObterCargosUsuarioId obterCargosUsuarioId;

    @Transactional
    public void atualizarCargos(List<String> cargosId, String idUsuario) {
        Set<String> atuais = obterCargosUsuarioId.obterCargosPorIdUsuario(idUsuario)
                .stream()
                .map(UsuarioCargoRetornoDTO::cargoId)
                .collect(Collectors.toSet());

        Set<String> desejados = new HashSet<>(cargosId);

        Set<String> toRemove = new HashSet<>(atuais);
        toRemove.removeAll(desejados);

        Set<String> toCreate = new HashSet<>(desejados);
        toCreate.removeAll(atuais);

        for (String cargoId : toRemove) {
            apagarUsuarioCargo.remover(new ApagarUsuarioCargoDTO(idUsuario, cargoId));
        }

        for (String cargoId : toCreate) {
            criarUsuarioCargo.criar(new CriarUsuarioCargoDTO(idUsuario, cargoId));
        }
    }
}