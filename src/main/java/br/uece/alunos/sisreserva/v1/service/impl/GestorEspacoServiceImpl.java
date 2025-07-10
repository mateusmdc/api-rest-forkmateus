package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.CadastroGestorEspaco;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.GestorEspacoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GestorEspacoServiceImpl implements GestorEspacoService {
    private final CadastroGestorEspaco cadastroGestorEspaco;
    @Override
    public GestorEspacoRetornoDTO cadastrarGestorEspaco(GestorEspacoDTO data) {
        return cadastroGestorEspaco.cadastrarGestorEspaco(data);
    }
}
