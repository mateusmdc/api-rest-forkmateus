package br.uece.alunos.sisreserva.v1.domain.departamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.departamento.DepartamentoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntDepartamentoPorId {
    @Autowired
    private DepartamentoRepository departamentoRepository;

    public Departamento obterEntidadePorId(String id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrado departamento com o ID informado."));
    }
}
