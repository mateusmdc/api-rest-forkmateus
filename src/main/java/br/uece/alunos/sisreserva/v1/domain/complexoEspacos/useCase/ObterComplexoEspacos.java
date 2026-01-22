package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.specification.ComplexoEspacosSpecification;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.validation.ComplexoEspacosValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ObterComplexoEspacos {
    private final ComplexoEspacosRepository repository;
    private final ComplexoEspacosValidator validator;

    public Page<ComplexoEspacosRetornoDTO> obter(Pageable pageable, String id, String nome) {
        validator.validarPermissaoParaListar();
        
        var spec = ComplexoEspacosSpecification.byFilter(id, nome);
        
        var complexos = repository.findAll(spec, pageable);
        
        return complexos.map(ComplexoEspacosRetornoDTO::new);
    }
}
