package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.secretariaEspaco.SecretariaEspacoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface do serviço de SecretariaEspaco.
 * Define os contratos para operações relacionadas à secretaria de espaços.
 */
public interface SecretariaEspacoService {
    
    /**
     * Cadastra uma nova secretaria de espaço ou reativa uma existente inativa.
     * 
     * @param data Dados do usuário e espaço a serem vinculados
     * @return DTO com os dados da secretaria cadastrada ou reativada
     */
    SecretariaEspacoRetornoDTO cadastrarOuReativarSecretariaEspaco(SecretariaEspacoDTO data);
    
    /**
     * Inativa uma secretaria de espaço.
     * 
     * @param secretariaEspacoId ID da secretaria a ser inativada
     * @return DTO com os dados da secretaria inativada
     */
    SecretariaEspacoRetornoDTO inativar(String secretariaEspacoId);
    
    /**
     * Obtém secretarias de espaço com filtros e paginação.
     * 
     * @param pageable Informações de paginação e ordenação
     * @param id Filtro por ID da secretaria (opcional)
     * @param espacoId Filtro por ID do espaço (opcional)
     * @param secretariaId Filtro por ID do usuário da secretaria (opcional)
     * @param todos Se true, inclui registros inativos
     * @return Página com os dados das secretarias encontradas
     */
    Page<SecretariaEspacoRetornoDTO> obter(Pageable pageable, String id, String espacoId, String secretariaId, boolean todos);
    
    /**
     * Valida se um usuário é membro ativo da secretaria de um espaço.
     * 
     * @param usuarioId ID do usuário a ser validado
     * @param espacoId ID do espaço
     * @throws br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException
     *         se o usuário não for membro ativo da secretaria
     */
    void validarSecretariaAtiva(String usuarioId, String espacoId);
}
