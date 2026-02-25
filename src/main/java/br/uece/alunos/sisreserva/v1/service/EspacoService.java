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
    
    // Método para obter estatísticas de uso dos espaços em um período
    EstatisticasGeralDTO obterEstatisticas(Integer mesInicial, Integer anoInicial, Integer mesFinal, Integer anoFinal, List<String> espacoIds, String departamentoId, String localizacaoId, String tipoEspacoId);
    
    /**
     * Gera PDF com estatísticas de uso dos espaços.
     * 
     * @param mesInicial mês inicial para filtrar reservas (opcional, padrão = mês atual)
     * @param anoInicial ano inicial para filtrar reservas (opcional, padrão = ano atual)
     * @param mesFinal mês final para filtrar reservas (opcional, padrão = mês atual)
     * @param anoFinal ano final para filtrar reservas (opcional, padrão = ano atual)
     * @param espacoIds lista de IDs de espaços para filtrar (opcional, padrão = todos os espaços)
     * @param departamentoId ID do departamento para filtrar espaços (opcional)
     * @param localizacaoId ID da localização para filtrar espaços (opcional)
     * @param tipoEspacoId ID do tipo de espaço para filtrar (opcional)
     * @return array de bytes contendo o PDF gerado
     * @throws java.io.IOException se houver erro na geração do PDF
     */
    byte[] gerarPDFEstatisticas(Integer mesInicial, Integer anoInicial, Integer mesFinal, Integer anoFinal, List<String> espacoIds, String departamentoId, String localizacaoId, String tipoEspacoId) throws java.io.IOException;
    
    // Método para deletar espaço (apenas administradores)
    void deletar(String id);
}
