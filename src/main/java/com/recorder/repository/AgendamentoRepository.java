import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

	// Consulta por usuário (mantido igual - JPA/Hibernate trata a conversão)
	List<Agendamento> findByUsuario(Usuario usuario);

	// Consulta por email (otimizada para PostgreSQL)
	@Query("SELECT a FROM Agendamento a JOIN FETCH a.usuario u " +
			"WHERE lower(u.email) = lower(:email)")
	List<Agendamento> findByUsuarioEmail(@Param("email") String email);

	// Nova consulta nativa para PostgreSQL (exemplo)
	@Query(value = "SELECT * FROM agendamentos a " +
			"WHERE to_tsvector('portuguese', a.local) @@ to_tsquery('portuguese', :termo)", nativeQuery = true)
	List<Agendamento> buscarPorLocal(@Param("termo") String termo);
}