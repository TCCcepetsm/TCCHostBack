package com.recorder.repository;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

	List<Agendamento> findByUsuario(Usuario usuario);

	@Query("SELECT a FROM Agendamento a JOIN FETCH a.usuario u WHERE lower(u.email) = lower(:email)")
	Page<Agendamento> findByUsuarioEmail(@Param("email") String email, Pageable pageable);

	@Query("SELECT a FROM Agendamento a JOIN FETCH a.usuario u WHERE a.id = :id AND lower(u.email) = lower(:email)")
	Optional<Agendamento> findByIdAndUsuarioEmail(@Param("id") Long id, @Param("email") String email);

	boolean existsByDataAndHorarioAndLocal(LocalDate data, LocalTime horario, String local);

	@Query(value = "SELECT * FROM agendamentos a WHERE to_tsvector('portuguese', a.local) @@ to_tsquery('portuguese', :termo)", nativeQuery = true)
	List<Agendamento> buscarPorLocal(@Param("termo") String termo);

	List<Agendamento> findByStatusAndDataBefore(StatusAgendamento status, LocalDate data);
}