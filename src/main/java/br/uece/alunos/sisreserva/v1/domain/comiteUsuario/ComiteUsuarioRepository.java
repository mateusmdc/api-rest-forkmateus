package br.uece.alunos.sisreserva.v1.domain.comiteUsuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComiteUsuarioRepository extends JpaRepository<ComiteUsuario, String>, JpaSpecificationExecutor<ComiteUsuario> {
    @Query("SELECT cu FROM ComiteUsuario cu WHERE cu.comite.id = :comiteId")
    List<ComiteUsuario> findByComiteId(String comiteId);

    @Query("SELECT cu FROM ComiteUsuario cu WHERE cu.usuario.id = :usuarioId")
    List<ComiteUsuario> findByUsuarioId(String usuarioId);

    @Query("SELECT cu FROM ComiteUsuario cu WHERE cu.departamento.id = :departamentoId")
    List<ComiteUsuario> findByDepartamentoId(String departamentoId);

    @Query("SELECT cu FROM ComiteUsuario cu WHERE cu.comite.id = :comiteId AND cu.isTitular = true")
    List<ComiteUsuario> findByComiteIdAndIsTitularTrue(String comiteId);

    @Query("SELECT cu FROM ComiteUsuario cu WHERE cu.comite.id = :comiteId AND cu.isTitular = false")
    List<ComiteUsuario> findByComiteIdAndIsTitularFalse(String comiteId);

    @Query("SELECT cu FROM ComiteUsuario cu WHERE cu.comite.id = :comiteId AND cu.isTitular = true ORDER BY cu.id")
    ComiteUsuario findTitularByComiteId(String comiteId);

    @Query("SELECT cu FROM ComiteUsuario cu ORDER BY cu.id")
    Page<ComiteUsuario> findAllPageable(Pageable pageable);
}