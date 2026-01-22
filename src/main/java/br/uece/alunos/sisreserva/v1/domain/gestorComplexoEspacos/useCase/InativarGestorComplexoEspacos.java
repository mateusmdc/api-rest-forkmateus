package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.validation.GestorComplexoEspacosValidator;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosRetornoDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Use case responsável por inativar um gestor de complexo de espaços.
 * Valida se o gestor está ativo antes de inativá-lo.
 */
@Slf4j
@Component
@AllArgsConstructor
public class InativarGestorComplexoEspacos {
    private final GestorComplexoEspacosRepository repository;
    private final GestorComplexoEspacosValidator validator;

    public GestorComplexoEspacosRetornoDTO inativar(String gestorComplexoEspacosId) {
        log.info("Iniciando inativação de gestor de complexo - ID: {}", gestorComplexoEspacosId);

        validator.validarPermissaoAdmin();
        validator.validarGestorAtivoParaInativar(gestorComplexoEspacosId);

        var gestor = repository.findById(gestorComplexoEspacosId)
                        .orElseThrow(() -> new IllegalStateException("Gestor de Complexo de Espaços deveria existir após validação, mas não foi encontrado."));

        gestor.setEstaAtivo(false);

        var gestorInativado = repository.save(gestor);

        log.info("Gestor de complexo inativado com sucesso - ID: {}", gestorInativado.getId());
        return new GestorComplexoEspacosRetornoDTO(gestorInativado);
    }

}
