import com.recorder.controller.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Consulta otimizada para PostgreSQL (case-insensitive)
    boolean existsByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "roles")
    @Query("SELECT u FROM Usuario u WHERE lower(u.email) = lower(:email)")
    Optional<Usuario> findByEmail(@Param("email") String email);

    // Exemplo de atualização específica para PostgreSQL
    @Modifying
    @Query(value = "UPDATE usuarios SET ultimo_acesso = NOW() AT TIME ZONE 'America/Sao_Paulo' WHERE id = :id", nativeQuery = true)
    void registrarAcesso(@Param("id") Long id);
}