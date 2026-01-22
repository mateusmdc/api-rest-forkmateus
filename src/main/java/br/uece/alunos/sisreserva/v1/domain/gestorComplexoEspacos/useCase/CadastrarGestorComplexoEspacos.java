package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.validation.GestorComplexoEspacosValidator;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Use case responsável por cadastrar um novo gestor para um complexo de espaços.
 * Valida se o gestor já não existe ativo antes de criar.
 */
@Slf4j
@Component
public class CadastrarGestorComplexoEspacos {
    @Autowired
    private GestorComplexoEspacosRepository repository;

    @Autowired
    private EntityHandlerService entityHandlerService;

    @Autowired
    private GestorComplexoEspacosValidator validator;

    public GestorComplexoEspacosRetornoDTO cadastrarGestorComplexoEspacos(GestorComplexoEspacosDTO data) {
        log.info("Iniciando cadastro de gestor de complexo - Usuário: {}, Complexo: {}", 
                data.usuarioGestorId(), data.complexoEspacosId());

        validator.validarGestorAtivoExistente(data.usuarioGestorId(), data.complexoEspacosId());

        var usuarioGestor = entityHandlerService.obterUsuarioPorId(data.usuarioGestorId());
        var complexoEspacos = entityHandlerService.obterComplexoEspacosPorId(data.complexoEspacosId());

        var novoGestor = new GestorComplexoEspacos(usuarioGestor, complexoEspacos);

        var gestorComplexoEspacosSalvo = repository.save(novoGestor);

        log.info("Gestor de complexo cadastrado com sucesso - ID: {}", gestorComplexoEspacosSalvo.getId());
        return new GestorComplexoEspacosRetornoDTO(gestorComplexoEspacosSalvo);
    }
}
