package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.ValidadorGestorEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.CriarEquipamentoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.EquipamentoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CriarEquipamentoEspaco {
    private final EntityHandlerService entityHandlerService;
    private final EquipamentoEspacoRepository repository;
    private final EquipamentoService equipamentoService;
    private final ValidadorGestorEspaco validaSeGestorEspaco;

    public List<EquipamentoEspacoRetornoDTO> criarEquipamentosAlocandoAoEspaco(CriarEquipamentoEspacoDTO data) {
        validaSeGestorEspaco.validarGestorAtivo(data.usuarioId(), data.espacoId());

        var espacoEntidade = entityHandlerService.obterEspacoPorId(data.espacoId());

        return data.equipamentos().stream()
                .map(equipamentoDTO -> {
                    var equipamentoNoBanco = equipamentoService.criar(equipamentoDTO);

                    var equipamentoEntidade = entityHandlerService.obterEquipamentoPorId(equipamentoNoBanco.id());
                    var equipamentoEspaco = new EquipamentoEspaco(equipamentoEntidade, espacoEntidade);

                    var salvo = repository.save(equipamentoEspaco);

                    return new EquipamentoEspacoRetornoDTO(salvo);
                })
                .toList();
    }
}
