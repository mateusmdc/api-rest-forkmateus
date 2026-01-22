package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.validation.ComplexoEspacosValidator;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarComplexoEspacos {
    private final ComplexoEspacosRepository repository;
    private final ComplexoEspacosValidator validator;

    public ComplexoEspacosRetornoDTO atualizar(String id, ComplexoEspacosAtualizarDTO data) {
        var complexo = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Complexo de espaços não encontrado"));

        validator.validarPermissaoParaModificarComplexo(id);
        validator.validarSeComplexoJaExisteParaAtualizacao(id, data.nome());

        complexo.atualizar(data);

        var complexoAtualizado = repository.save(complexo);

        return new ComplexoEspacosRetornoDTO(complexoAtualizado);
    }
}
