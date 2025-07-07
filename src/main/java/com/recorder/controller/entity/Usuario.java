package com.recorder.controller.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.recorder.controller.entity.enuns.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors; // Adicione esta importação se ainda não tiver
// Adicione esta importação se ainda não tiver
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "cpf")
})
public class Usuario implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;

	@NotBlank(message = "Nome é obrigatório")
	@Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
	private String nome;

	@Email(message = "Email deve ser válido")
	@NotBlank(message = "Email é obrigatório")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "Telefone é obrigatório")
	@Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$", message = "Telefone deve estar no formato (99) 99999-9999")
	private String telefone;

	@NotBlank(message = "CPF é obrigatório")
	@Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}", message = "CPF deve estar no formato 999.999.999-99")
	@Column(unique = true)
	private String cpf;

	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
	private String senha;

	@Builder.Default
	@Column(name = "ativo", nullable = false)
	private boolean ativo = true;

	@Builder.Default
	@ElementCollection(targetClass = Roles.class)
	@CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Set<Roles> roles = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private Set<Agendamento> agendamentos = new HashSet<>(); // <-- MUDOU DE List para Set

	// Métodos do UserDetails
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getAuthority()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public List<String> getRolesAsStrings() {
		return this.roles.stream()
				.map(roleEnum -> roleEnum.getAuthority()) // Pega a string da autoridade do enum Roles
				.collect(Collectors.toList());
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return ativo;
	}

	// Métodos utilitários
	public void adicionarRole(Roles role) {
		roles.add(role);
	}

	public void removerRole(Roles role) {
		roles.remove(role);
	}

	public boolean temRole(Roles role) {
		return roles.contains(role);
	}

	// Equals e hashCode otimizados
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Usuario))
			return false;
		Usuario usuario = (Usuario) o;
		return Objects.equals(id, usuario.id) &&
				Objects.equals(email, usuario.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}
}
