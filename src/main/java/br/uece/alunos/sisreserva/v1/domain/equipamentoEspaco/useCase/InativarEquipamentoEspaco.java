package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.validation.EquipamentoEspacoValidator;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.ValidadorGestorEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class InativarEquipamentoEspaco {
    private final EquipamentoEspacoRepository repository;
    private final EquipamentoEspacoValidator validator;
    private final ValidadorGestorEspaco validaSeGestorEspaco;

    public EquipamentoEspacoRetornoDTO inativar(String equipamentoEspacoId, String usuarioId) {
        validator.validarEquipamentoEspacoId(equipamentoEspacoId);

        var equipamentoEspaco = repository.findById(equipamentoEspacoId)
                .orElseThrow(() -> new IllegalStateException("EquipamentoEspaço deveria existir após validação, mas não foi encontrado."));

        var espacoId = equipamentoEspaco.getEspaco().getId();
        validaSeGestorEspaco.validarGestorAtivo(usuarioId, espacoId);

        equipamentoEspaco.setDataRemocao(LocalDateTime.now());

        var removido = repository.save(equipamentoEspaco);

        return new EquipamentoEspacoRetornoDTO(removido);
    }
}
