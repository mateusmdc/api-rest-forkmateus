package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosRetornoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Use case responsável por reativar um gestor de complexo de espaços que estava inativo.
 */
@Slf4j
@Component
public class ReativarGestorComplexoEspacos {
    @Autowired
    private GestorComplexoEspacosRepository repository;

    public GestorComplexoEspacosRetornoDTO reativar(GestorComplexoEspacos gestorInativo) {
        log.info("Reativando gestor de complexo - ID: {}", gestorInativo.getId());

        gestorInativo.setEstaAtivo(true);
        gestorInativo.setDeletedAt(null);

        var gestorReativadoNoBanco = repository.save(gestorInativo);

        log.info("Gestor de complexo reativado com sucesso - ID: {}", gestorReativadoNoBanco.getId());
        return new GestorComplexoEspacosRetornoDTO(gestorReativadoNoBanco);
    }
}
