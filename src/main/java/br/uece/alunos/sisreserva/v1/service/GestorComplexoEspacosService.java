package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface de serviço para operações relacionadas a gestores de complexos de espaços.
 * Define os contratos para cadastro, inativação e consulta de gestores.
 */
public interface GestorComplexoEspacosService {
    
    /**
     * Cadastra um novo gestor ou reativa um gestor inativo para um complexo de espaços.
     *
     * @param data DTO contendo usuarioGestorId e complexoEspacosId
     * @return DTO com dados do gestor cadastrado/reativado
     */
    GestorComplexoEspacosRetornoDTO cadastrarOuReativarGestorComplexoEspacos(GestorComplexoEspacosDTO data);
    
    /**
     * Inativa um gestor de complexo de espaços.
     *
     * @param gestorComplexoEspacosId ID do gestor a ser inativado
     * @return DTO com dados do gestor inativado
     */
    GestorComplexoEspacosRetornoDTO inativar(String gestorComplexoEspacosId);
    
    /**
     * Obtém gestores de complexos com filtros opcionais.
     *
     * @param pageable configuração de paginação
     * @param id filtro por ID do gestor
     * @param complexoEspacosId filtro por ID do complexo
     * @param gestorId filtro por ID do usuário gestor
     * @param todos se true, inclui gestores inativos
     * @return página com gestores encontrados
     */
    Page<GestorComplexoEspacosRetornoDTO> obter(Pageable pageable, String id, String complexoEspacosId, String gestorId, boolean todos);
    
    /**
     * Valida se um usuário é gestor ativo de um complexo.
     *
     * @param usuarioId ID do usuário
     * @param complexoEspacosId ID do complexo
     * @throws br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException se não for gestor ativo
     */
    void validarGestorAtivo(String usuarioId, String complexoEspacosId);
}
