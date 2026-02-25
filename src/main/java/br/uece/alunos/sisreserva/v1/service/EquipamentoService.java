package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasGeralEquipamentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipamentoService {
    EquipamentoRetornoDTO atualizar(String id, EquipamentoAtualizarDTO data);
    EquipamentoRetornoDTO criar(EquipamentoDTO data);
    void deletar(String id);
    Page<EquipamentoRetornoDTO> obter(Pageable pageable, String id, String tombamento, String status, String tipoEquipamento, Boolean reservavel);
    
    /**
     * Obtém estatísticas de uso dos equipamentos em um período.
     * 
     * @param mesInicial mês inicial para filtrar reservas (opcional, padrão = mês atual)
     * @param anoInicial ano inicial para filtrar reservas (opcional, padrão = ano atual)
     * @param mesFinal mês final para filtrar reservas (opcional, padrão = mês atual)
     * @param anoFinal ano final para filtrar reservas (opcional, padrão = ano atual)
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @param tipoEquipamentoId ID do tipo de equipamento para filtrar (opcional)
     * @param multiusuario filtrar por equipamentos multiusuário (opcional)
     * @param espacoId ID do espaço vinculado para filtrar equipamentos (opcional)
     * @return estatísticas agrupadas por equipamento
     */
    EstatisticasGeralEquipamentoDTO obterEstatisticas(Integer mesInicial, Integer anoInicial, Integer mesFinal, Integer anoFinal, List<String> equipamentoIds, String tipoEquipamentoId, Boolean multiusuario, String espacoId);
    
    /**
     * Gera PDF com estatísticas de uso dos equipamentos.
     * 
     * @param mesInicial mês inicial para filtrar reservas (opcional, padrão = mês atual)
     * @param anoInicial ano inicial para filtrar reservas (opcional, padrão = ano atual)
     * @param mesFinal mês final para filtrar reservas (opcional, padrão = mês atual)
     * @param anoFinal ano final para filtrar reservas (opcional, padrão = ano atual)
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @param tipoEquipamentoId ID do tipo de equipamento para filtrar (opcional)
     * @param multiusuario filtrar por equipamentos multiusuário (opcional)
     * @param espacoId ID do espaço vinculado para filtrar equipamentos (opcional)
     * @return array de bytes contendo o PDF gerado
     * @throws java.io.IOException se houver erro na geração do PDF
     */
    byte[] gerarPDFEstatisticas(Integer mesInicial, Integer anoInicial, Integer mesFinal, Integer anoFinal, List<String> equipamentoIds, String tipoEquipamentoId, Boolean multiusuario, String espacoId) throws java.io.IOException;
    
    /**
     * Obtém equipamentos reserváveis com filtros e paginação.
     * Retorna apenas equipamentos com o campo 'reservavel' definido como true.
     * Para usuários externos, também aplica restrição de 'multiusuario' = true.
     * 
     * @param pageable Informações de paginação e ordenação
     * @param id Filtro por ID do equipamento (opcional)
     * @param tombamento Filtro por tombamento do equipamento (opcional)
     * @param status Filtro por status do equipamento (opcional)
     * @param tipoEquipamento Filtro por ID do tipo de equipamento (opcional)
     * @param multiusuario Filtro explícito por equipamentos multiusuário (opcional)
     * @return Página com os equipamentos reserváveis encontrados
     */
    Page<EquipamentoRetornoDTO> obterEquipamentosReservaveis(Pageable pageable,
                                                             String id,
                                                             String tombamento,
                                                             String status,
                                                             String tipoEquipamento,
                                                             Boolean multiusuario);
}
