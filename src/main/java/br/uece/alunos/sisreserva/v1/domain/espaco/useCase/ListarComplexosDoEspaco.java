package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListarComplexosDoEspaco {
    private final EspacoRepository espacoRepository;

    public List<ComplexoEspacosRetornoDTO> listar(String espacoId) {
        var espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado"));

        return espaco.getComplexos()
                .stream()
                .map(ComplexoEspacosRetornoDTO::new)
                .toList();
    }
}
