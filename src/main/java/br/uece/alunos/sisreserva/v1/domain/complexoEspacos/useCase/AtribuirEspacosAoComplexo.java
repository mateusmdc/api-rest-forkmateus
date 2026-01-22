package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.validation.ComplexoEspacosValidator;

import java.util.List;

@Component
@AllArgsConstructor
public class AtribuirEspacosAoComplexo {
    private final ComplexoEspacosRepository complexoRepository;
    private final EspacoRepository espacoRepository;
    private final ComplexoEspacosValidator validator;

    public ComplexoEspacosRetornoDTO atribuir(String complexoId, List<String> espacoIds) {
        var complexo = complexoRepository.findById(complexoId)
                .orElseThrow(() -> new ValidationException("Complexo de espaços não encontrado"));

        validator.validarPermissaoParaModificarComplexo(complexoId);

        var espacos = espacoRepository.findAllById(espacoIds);

        if (espacos.size() != espacoIds.size()) {
            throw new ValidationException("Um ou mais espaços não foram encontrados");
        }

        // Adiciona os novos espaços sem remover os existentes
        espacos.forEach(espaco -> {
            if (!complexo.getEspacos().contains(espaco)) {
                complexo.getEspacos().add(espaco);
            }
        });

        var complexoSalvo = complexoRepository.save(complexo);

        return new ComplexoEspacosRetornoDTO(complexoSalvo);
    }
}
