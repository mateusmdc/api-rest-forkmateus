package br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.validation.TipoEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarTipoEspaco {
    private final TipoEspacoRepository repository;
    private final TipoEspacoValidator validator;
    private final EntityHandlerService entityHandlerService;

    public TipoEspacoRetornoDTO atualizar(String id, TipoEspacoAtualizarDTO data) {
        var tipoEspaco = entityHandlerService.obterTipoEspacoPorId(id);
        
        validator.validarSeTipoEspacoJaExisteParaAtualizacao(id, data.nome());
        
        tipoEspaco.atualizar(data);
        
        var tipoEspacoAtualizado = repository.save(tipoEspaco);
        
        return new TipoEspacoRetornoDTO(tipoEspacoAtualizado);
    }
}
