package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade SecretariaEspaco.
 * Fornece operações de consulta e manipulação de dados relacionados à secretaria de espaços.
 */
public interface SecretariaEspacoRepository extends JpaRepository<SecretariaEspaco, String>, JpaSpecificationExecutor<SecretariaEspaco> {
    
    /**
     * Busca todas as secretarias de espaço associadas a um usuário específico
     * 
     * @param usuarioId ID do usuário da secretaria
     * @return Lista de secretarias de espaço do usuário
     */
    @Query("SELECT s FROM SecretariaEspaco s WHERE s.usuarioSecretaria.id = :usuarioId")
    List<SecretariaEspaco> findByUsuarioSecretariaId(String usuarioId);

    /**
     * Busca todas as secretarias de espaço de um espaço específico
     * 
     * @param espacoId ID do espaço
     * @return Lista de secretarias do espaço
     */
    @Query("SELECT s FROM SecretariaEspaco s WHERE s.espaco.id = :espacoId")
    List<SecretariaEspaco> findByEspacoId(String espacoId);

    /**
     * Busca todos os usuários ativos da secretaria de um espaço, carregando os dados do usuário
     * 
     * @param espacoId ID do espaço
     * @return Lista de secretarias ativas do espaço com os dados do usuário carregados
     */
    @Query("""
        SELECT s FROM SecretariaEspaco s
        JOIN FETCH s.usuarioSecretaria
        WHERE s.espaco.id = :espacoId
          AND s.estaAtivo = true
    """)
    List<SecretariaEspaco> findSecretariasAtivasComUsuarioByEspacoId(String espacoId);

    /**
     * Busca todas as secretarias de espaço ordenadas por ID
     * 
     * @param pageable Informações de paginação
     * @return Página de secretarias ordenadas por ID
     */
    @Query("SELECT s FROM SecretariaEspaco s ORDER BY s.id ASC")
    Page<SecretariaEspaco> findAllOrderedById(Pageable pageable);

    /**
     * Verifica se existe uma secretaria ativa para o usuário e espaço especificados
     * 
     * @param usuarioId ID do usuário
     * @param espacoId ID do espaço
     * @return true se existe uma secretaria ativa, false caso contrário
     */
    @Query("""
        SELECT COUNT(s) > 0 FROM SecretariaEspaco s
        WHERE s.usuarioSecretaria.id = :usuarioId
          AND s.espaco.id = :espacoId
          AND s.estaAtivo = true
    """)
    boolean existsByUsuarioSecretariaIdAndEspacoIdAndEstaAtivoTrue(String usuarioId, String espacoId);

    /**
     * Busca uma secretaria inativa para o usuário e espaço especificados
     * 
     * @param usuarioId ID do usuário
     * @param espacoId ID do espaço
     * @return Optional contendo a secretaria inativa, se existir
     */
    @Query("""
        SELECT s FROM SecretariaEspaco s 
        WHERE s.usuarioSecretaria.id = :usuarioId 
          AND s.espaco.id = :espacoId 
          AND s.estaAtivo = false
        """)
    Optional<SecretariaEspaco> findByUsuarioSecretariaIdAndEspacoIdAndEstaAtivoFalse(String usuarioId, String espacoId);

    /**
     * Busca uma secretaria ativa para o usuário e espaço especificados
     * 
     * @param usuarioId ID do usuário
     * @param espacoId ID do espaço
     * @return Optional contendo a secretaria ativa, se existir
     */
    @Query("""
        SELECT s FROM SecretariaEspaco s 
        WHERE s.usuarioSecretaria.id = :usuarioId 
          AND s.espaco.id = :espacoId 
          AND s.estaAtivo = true
        """)
    Optional<SecretariaEspaco> findByUsuarioSecretariaIdAndEspacoIdAndEstaAtivo(String usuarioId, String espacoId);
}
