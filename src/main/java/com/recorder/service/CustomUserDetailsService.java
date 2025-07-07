import com.recorder.controller.entity.Usuario;
import com.recorder.exception.CustomAuthenticationException;
import com.recorder.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Consulta otimizada para PostgreSQL com case-insensitive
		Usuario usuario = usuarioRepository.findByEmailWithRoles(username.toLowerCase())
				.orElseThrow(() -> {
					String message = "Credenciais inválidas para: " + username;
					return new CustomAuthenticationException(message, HttpStatus.UNAUTHORIZED);
				});

		if (!usuario.isAtivo()) {
			throw new CustomAuthenticationException(
					"Usuário desativado",
					HttpStatus.FORBIDDEN);
		}

		List<GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(role -> {
					String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
					return new SimpleGrantedAuthority(roleName);
				})
				.collect(Collectors.toList());

		return User.builder()
				.username(usuario.getEmail())
				.password(usuario.getSenha())
				.authorities(authorities)
				.accountLocked(!usuario.isAtivo())
				.build();
	}
}