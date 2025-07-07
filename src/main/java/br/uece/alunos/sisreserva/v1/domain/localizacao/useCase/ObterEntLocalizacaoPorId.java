package br.uece.alunos.sisreserva.v1.domain.localizacao.useCase;

import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;
import br.uece.alunos.sisreserva.v1.domain.localizacao.LocalizacaoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntLocalizacaoPorId {
    @Autowired
    private LocalizacaoRepository repository;

    public Localizacao obterEntidadePorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrada localização com o ID informado."));
    }
}
