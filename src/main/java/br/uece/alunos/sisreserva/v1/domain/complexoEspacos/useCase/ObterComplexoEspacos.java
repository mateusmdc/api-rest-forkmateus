package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.specification.ComplexoEspacosSpecification;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ObterComplexoEspacos {
    private final ComplexoEspacosRepository repository;

    public Page<ComplexoEspacosRetornoDTO> obter(Pageable pageable, String id, String nome) {
        var spec = ComplexoEspacosSpecification.byFilter(id, nome);
        
        var complexos = repository.findAll(spec, pageable);
        
        return complexos.map(ComplexoEspacosRetornoDTO::new);
    }
}
