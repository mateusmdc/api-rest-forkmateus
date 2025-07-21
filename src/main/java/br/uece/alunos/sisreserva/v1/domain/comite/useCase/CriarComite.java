package br.uece.alunos.sisreserva.v1.domain.comite.useCase;

import br.uece.alunos.sisreserva.v1.domain.comite.Comite;
import br.uece.alunos.sisreserva.v1.domain.comite.ComiteRepository;
import br.uece.alunos.sisreserva.v1.domain.comite.validation.ComiteValidator;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteDTO;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarComite {
    @Autowired
    private ComiteRepository repository;
    @Autowired
    private ComiteValidator validator;

    public ComiteRetornoDTO criar(ComiteDTO data) {
        validator.validarDescricaoDuplicada(data);

        var comite = new Comite(data);
        var comiteNoBanco = repository.save(comite);

        return new ComiteRetornoDTO(comiteNoBanco);
    }
}
