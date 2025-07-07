package com.recorder.repository;

import com.recorder.controller.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List; // Importar List
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = { "roles" })
    @Query("SELECT u FROM Usuario u WHERE lower(u.email) = lower(:email)")
    Optional<Usuario> findByEmailWithRoles(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE lower(u.email) = lower(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.ultimoAcesso = CURRENT_TIMESTAMP WHERE u.id = :id")
    void registrarAcesso(@Param("id") Long id);

    // Consulta nativa otimizada para PostgreSQL
    @Query(value = """
            SELECT u.* FROM usuarios u
            JOIN usuario_roles ur ON u.id = ur.usuario_id
            JOIN roles r ON ur.role_id = r.id
            WHERE lower(u.email) = lower(:email)
            """, nativeQuery = true)
    Optional<Usuario> findByEmailWithRolesNative(@Param("email") String email);

    // Método básico para encontrar por email (case-sensitive)
    Optional<Usuario> findByEmail(String email);

    // --- Métodos ausentes que precisam ser adicionados ---

    @EntityGraph(attributePaths = { "roles" })
    List<Usuario> findAllUsuariosWithRoles();

    @EntityGraph(attributePaths = { "roles" })
    Optional<Usuario> findByIdWithRoles(Long id);
}