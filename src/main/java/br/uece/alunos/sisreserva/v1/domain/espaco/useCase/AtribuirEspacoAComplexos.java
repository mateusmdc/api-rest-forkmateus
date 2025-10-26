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
public class AtribuirEspacoAComplexos {
    private final EspacoRepository espacoRepository;
    private final ComplexoEspacosRepository complexoRepository;

    public EspacoRetornoDTO atribuir(String espacoId, List<String> complexoIds) {
        var espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado"));

        var complexos = complexoRepository.findAllById(complexoIds);

        if (complexos.size() != complexoIds.size()) {
            throw new ValidationException("Um ou mais complexos não foram encontrados");
        }

        // Adiciona os novos complexos sem remover os existentes
        complexos.forEach(complexo -> {
            if (!espaco.getComplexos().contains(complexo)) {
                espaco.getComplexos().add(complexo);
            }
        });

        var espacoSalvo = espacoRepository.save(espaco);

        return new EspacoRetornoDTO(espacoSalvo);
    }
}
