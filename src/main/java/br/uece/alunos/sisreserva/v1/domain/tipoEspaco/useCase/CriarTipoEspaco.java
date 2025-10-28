package br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.validation.TipoEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CriarTipoEspaco {
    private final TipoEspacoRepository repository;
    private final TipoEspacoValidator validator;

    public TipoEspacoRetornoDTO criar(TipoEspacoDTO data) {
        validator.validarSeTipoEspacoJaExiste(data.nome());

        TipoEspaco tipoEspaco = new TipoEspaco();
        tipoEspaco.setNome(data.nome());

        var tipoEspacoSalvo = repository.save(tipoEspaco);

        return new TipoEspacoRetornoDTO(tipoEspacoSalvo);
    }
}
