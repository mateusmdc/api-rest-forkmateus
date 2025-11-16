package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DesatribuirEspacoDeComplexos {
    private final EspacoRepository espacoRepository;
    private final ComplexoEspacosRepository complexoRepository;

    public EspacoRetornoDTO desatribuir(String espacoId, List<String> complexoIds) {
        var espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado"));

        var complexos = complexoRepository.findAllById(complexoIds);

        if (complexos.size() != complexoIds.size()) {
            throw new ValidationException("Um ou mais complexos não foram encontrados");
        }

        // Remove o espaço do lado proprietário da relação (ComplexoEspacos)
        complexos.forEach(complexo -> {
            complexo.getEspacos().remove(espaco);
        });

        // Salva os complexos (lado proprietário)
        complexoRepository.saveAll(complexos);

        // Recarrega o espaço para obter as relações atualizadas
        var espacoAtualizado = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado"));

        return new EspacoRetornoDTO(espacoAtualizado);
    }
}
