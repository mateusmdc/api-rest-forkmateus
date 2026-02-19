package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasGeralDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EspacoService {
    EspacoRetornoDTO atualizar(String id, EspacoAtualizarDTO data);
    EspacoRetornoDTO criarEspaco(EspacoDTO data);
    Page<EspacoRetornoDTO> obterEspacos(Pageable pageable,
                                        String id,
                                        String departamento,
                                        String localizacao,
                                        String tipoEspaco,
                                        String tipoAtividade,
                                        String nome,
                                        Boolean multiusuario,
                                        Boolean reservavel);
    
    // Novo método para horários ocupados do espaço
    HorariosOcupadosPorMesDTO obterHorariosOcupadosPorEspaco(String espacoId, Integer mes, Integer ano);
    
    // Métodos para gerenciar complexos
    EspacoRetornoDTO atribuirComplexos(String id, List<String> complexoIds);
    EspacoRetornoDTO desatribuirComplexos(String id, List<String> complexoIds);
    List<ComplexoEspacosRetornoDTO> listarComplexos(String id);
    
    // Método para obter estatísticas de uso dos espaços
    EstatisticasGeralDTO obterEstatisticas(Integer mes, Integer ano, List<String> espacoIds);
    
    // Método para deletar espaço (apenas administradores)
    void deletar(String id);
}
