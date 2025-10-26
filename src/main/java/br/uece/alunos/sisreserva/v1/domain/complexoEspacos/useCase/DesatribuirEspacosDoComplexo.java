package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DesatribuirEspacosDoComplexo {
    private final ComplexoEspacosRepository complexoRepository;
    private final EspacoRepository espacoRepository;

    public ComplexoEspacosRetornoDTO desatribuir(String complexoId, List<String> espacoIds) {
        var complexo = complexoRepository.findById(complexoId)
                .orElseThrow(() -> new ValidationException("Complexo de espaços não encontrado"));

        var espacos = espacoRepository.findAllById(espacoIds);

        if (espacos.size() != espacoIds.size()) {
            throw new ValidationException("Um ou mais espaços não foram encontrados");
        }

        // Remove os espaços especificados
        complexo.getEspacos().removeAll(espacos);

        var complexoSalvo = complexoRepository.save(complexo);

        return new ComplexoEspacosRetornoDTO(complexoSalvo);
    }
}
