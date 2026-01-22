package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.validation.GestorComplexoEspacosValidator;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosRetornoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Use case que decide entre cadastrar um novo gestor ou reativar um gestor inativo.
 * Se existir um gestor inativo para a combinação usuário-complexo, reativa.
 * Caso contrário, cria um novo registro.
 */
@Slf4j
@Component
public class CadastraOuReativaGestorComplexoEspacos {
    @Autowired
    private GestorComplexoEspacosRepository repository;

    @Autowired
    private GestorComplexoEspacosValidator validator;

    @Autowired
    private CadastrarGestorComplexoEspacos cadastrarGestorComplexoEspacos;

    @Autowired
    private ReativarGestorComplexoEspacos reativarGestorComplexoEspacos;

    public GestorComplexoEspacosRetornoDTO executar(GestorComplexoEspacosDTO data) {
        log.info("Executando cadastro ou reativação de gestor de complexo - Usuário: {}, Complexo: {}", 
                data.usuarioGestorId(), data.complexoEspacosId());

        validator.validarPermissaoAdmin();

        var gestorInativoOpt = repository.findByUsuarioGestorIdAndComplexoEspacosIdAndEstaAtivoFalse(
                data.usuarioGestorId(), data.complexoEspacosId());

        if (gestorInativoOpt.isPresent()) {
            log.info("Gestor inativo encontrado, reativando...");
            return reativarGestorComplexoEspacos.reativar(gestorInativoOpt.get());
        }

        log.info("Nenhum gestor inativo encontrado, cadastrando novo gestor...");
        return cadastrarGestorComplexoEspacos.cadastrarGestorComplexoEspacos(data);
    }
}
