package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.validation.ComplexoEspacosValidator;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CriarComplexoEspacos {
    private final ComplexoEspacosRepository repository;
    private final ComplexoEspacosValidator validator;

    public ComplexoEspacosRetornoDTO criar(ComplexoEspacosDTO data) {
        validator.validarPermissaoAdmin();
        validator.validarSeComplexoJaExiste(data.nome());

        ComplexoEspacos complexo = new ComplexoEspacos();
        complexo.setNome(data.nome());
        complexo.setDescricao(data.descricao());
        complexo.setSite(data.site());

        var complexoSalvo = repository.save(complexo);

        return new ComplexoEspacosRetornoDTO(complexoSalvo);
    }
}
