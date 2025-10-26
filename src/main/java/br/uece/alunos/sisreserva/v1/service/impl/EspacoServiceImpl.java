package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.AtribuirEspacoAComplexos;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.AtualizarEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.CriarEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.DesatribuirEspacoDeComplexos;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ObterEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ObterHorariosOcupadosEspaco;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
import br.uece.alunos.sisreserva.v1.service.EspacoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EspacoServiceImpl implements EspacoService {
    private final AtualizarEspaco atualizarEspaco;
    private final CriarEspaco criarEspaco;
    private final ObterEspaco obterEspaco;
    private final ObterHorariosOcupadosEspaco obterHorariosOcupadosEspaco;
    private final AtribuirEspacoAComplexos atribuirEspacoAComplexos;
    private final DesatribuirEspacoDeComplexos desatribuirEspacoDeComplexos;

    @Override
    public EspacoRetornoDTO atualizar(String id, EspacoAtualizarDTO data) {
        return atualizarEspaco.atualizar(id, data);
    }

    @Override
    public EspacoRetornoDTO criarEspaco(EspacoDTO data) {
        return criarEspaco.criarEspaco(data);
    }

    @Override
    public Page<EspacoRetornoDTO> obterEspacos(Pageable pageable, String id, String departamento, String localizacao,
                                               String tipoEspaco, String tipoAtividade, String nome, Boolean multiusuario) {
        return obterEspaco.obterEspacos(pageable, id, departamento, localizacao, tipoEspaco, tipoAtividade, nome, multiusuario);
    }

    @Override
    public HorariosOcupadosPorMesDTO obterHorariosOcupadosPorEspaco(String espacoId, Integer mes, Integer ano) {
        return obterHorariosOcupadosEspaco.obterHorariosOcupadosPorEspaco(espacoId, mes, ano);
    }

    @Override
    public EspacoRetornoDTO atribuirComplexos(String id, List<String> complexoIds) {
        return atribuirEspacoAComplexos.atribuir(id, complexoIds);
    }

    @Override
    public EspacoRetornoDTO desatribuirComplexos(String id, List<String> complexoIds) {
        return desatribuirEspacoDeComplexos.desatribuir(id, complexoIds);
    }
}
