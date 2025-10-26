package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListarEspacosDoComplexo {
    private final ComplexoEspacosRepository complexoRepository;

    public List<EspacoRetornoDTO> listar(String complexoId) {
        var complexo = complexoRepository.findById(complexoId)
                .orElseThrow(() -> new ValidationException("Complexo não encontrado"));

        return complexo.getEspacos()
                .stream()
                .map(EspacoRetornoDTO::new)
                .toList();
    }
}
