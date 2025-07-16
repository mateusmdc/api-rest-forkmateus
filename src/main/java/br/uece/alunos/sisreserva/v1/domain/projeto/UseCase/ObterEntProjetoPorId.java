package br.uece.alunos.sisreserva.v1.domain.projeto.UseCase;

import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import br.uece.alunos.sisreserva.v1.domain.projeto.ProjetoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntProjetoPorId {
    @Autowired
    private ProjetoRepository repository;

    public Projeto obterEntidade(String id){
        return repository.findById(id)
        .orElseThrow(() -> new ValidationException(
            "Não foi encontrado projeto com o ID informado."
        ));
    }
}
