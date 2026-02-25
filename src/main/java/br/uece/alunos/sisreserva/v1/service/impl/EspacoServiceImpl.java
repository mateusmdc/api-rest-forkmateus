package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.AtribuirEspacoAComplexos;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.AtualizarEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.CriarEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.DeletarEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.DesatribuirEspacoDeComplexos;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ListarComplexosDoEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ObterEspaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ObterEstatisticasEspacos;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ObterHorariosOcupadosEspaco;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasGeralDTO;
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
    private final DeletarEspaco deletarEspaco;
    private final ObterEspaco obterEspaco;
    private final ObterHorariosOcupadosEspaco obterHorariosOcupadosEspaco;
    private final AtribuirEspacoAComplexos atribuirEspacoAComplexos;
    private final DesatribuirEspacoDeComplexos desatribuirEspacoDeComplexos;
    private final ListarComplexosDoEspaco listarComplexosDoEspaco;
    private final ObterEstatisticasEspacos obterEstatisticasEspacos;
    private final br.uece.alunos.sisreserva.v1.domain.espaco.useCase.GerarPDFEstatisticasEspacos gerarPDFEstatisticasEspacos;

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
                                               String tipoEspaco, String tipoAtividade, String nome, Boolean multiusuario, Boolean reservavel) {
        return obterEspaco.obterEspacos(pageable, id, departamento, localizacao, tipoEspaco, tipoAtividade, nome, multiusuario, reservavel);
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

    @Override
    public List<ComplexoEspacosRetornoDTO> listarComplexos(String id) {
        return listarComplexosDoEspaco.listar(id);
    }

    @Override
    public EstatisticasGeralDTO obterEstatisticas(Integer mesInicial, Integer anoInicial, Integer mesFinal, Integer anoFinal, List<String> espacoIds, String departamentoId, String localizacaoId, String tipoEspacoId) {
        return obterEstatisticasEspacos.obterEstatisticas(mesInicial, anoInicial, mesFinal, anoFinal, espacoIds, departamentoId, localizacaoId, tipoEspacoId);
    }

    @Override
    public byte[] gerarPDFEstatisticas(Integer mesInicial, Integer anoInicial, Integer mesFinal, Integer anoFinal, List<String> espacoIds, String departamentoId, String localizacaoId, String tipoEspacoId) throws java.io.IOException {
        return gerarPDFEstatisticasEspacos.gerarPDF(mesInicial, anoInicial, mesFinal, anoFinal, espacoIds, departamentoId, localizacaoId, tipoEspacoId);
    }

    @Override
    public void deletar(String id) {
        deletarEspaco.deletar(id);
    }
}
