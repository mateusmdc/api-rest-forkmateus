package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase;

import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.UsuarioCargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargo;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CriarEmCargaUsuarioCargo {
    @Autowired
    private UsuarioCargoRepository usuarioCargoRepository;
    @Autowired
    private EntityHandlerService entityHandlerService;

    public List<UsuarioCargoRetornoDTO> criarEmCargaUsuarioCargo(CriarCargaUsuarioCargoDTO data) {
        try {
            Set<String> nomesUnicos = new HashSet<>(data.cargosNome());
            if (nomesUnicos.size() < data.cargosNome().size()) {
                throw new ValidationException("Há cargos repetidos na lista de entrada.");
            }

            var usuario = entityHandlerService.obterUsuarioPorId(data.usuarioId());
            var cargos = entityHandlerService.obterEntidadesCargoPorNome(data.cargosNome());

            if (!usuario.isEnabled()) {
                throw new ValidationException("O usuário está inativo e não pode receber cargos.");
            }

            List<UsuarioCargo> novosUsuarioCargos = cargos.stream()
                    .filter(cargo -> !usuarioCargoRepository.existsByUsuarioIdAndCargoId(usuario.getId(), cargo.getId()))
                    .map(cargo -> new UsuarioCargo(usuario, cargo))
                    .toList();

            if (novosUsuarioCargos.isEmpty()) {
                throw new ValidationException("Todos os cargos informados já estão atribuídos ao usuário.");
            }

            List<UsuarioCargo> salvos = usuarioCargoRepository.saveAll(novosUsuarioCargos);

            return salvos.stream().map(UsuarioCargoRetornoDTO::new).toList();
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
