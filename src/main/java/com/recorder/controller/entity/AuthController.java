package com.recorder.controller.entity;

import com.recorder.dto.AuthenticationRequest;
import com.recorder.dto.AuthenticationResponse;
import com.recorder.config.JwtService;
import com.recorder.exception.AuthenticationFailedException;
import com.recorder.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para autenticação e validação de tokens")
public class AuthController {

	private final UsuarioRepository usuarioRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/authenticate")
	@Operation(summary = "Autenticar usuário", responses = {
			@ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida"),
			@ApiResponse(responseCode = "401", description = "Credenciais inválidas")
	})
	public ResponseEntity<AuthenticationResponse> authenticate(
			@Valid @RequestBody AuthenticationRequest request) {

		try {
			// 1. Autenticação básica
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getEmail(),
							request.getSenha()));

			// 2. Buscar usuário
			Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
					.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

			// 3. Gerar token JWT
			UserDetails userDetails = new User(
					usuario.getEmail(),
					usuario.getSenha(),
					usuario.getAuthorities());

			String jwtToken = jwtService.generateToken(userDetails);

			// 4. Log de sucesso (sem informações sensíveis)
			log.info("Autenticação bem-sucedida para usuário: {}", request.getEmail());

			// 5. Retornar resposta
			return ResponseEntity.ok(
					AuthenticationResponse.builder()
							.token(jwtToken)
							.email(usuario.getEmail())
							.nome(usuario.getNome())
							.roles(usuario.getRolesAsStrings()) // Método que deve ser implementado na classe Usuario
							.build());

		} catch (BadCredentialsException e) {
			log.warn("Tentativa de autenticação falhou para: {}", request.getEmail());
			throw new AuthenticationFailedException("Credenciais inválidas");
		}
	}

	@GetMapping("/validate-token")
	@Operation(summary = "Validar token JWT")
	public ResponseEntity<Void> validateToken(
			HttpServletRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {

		try {
			String authHeader = request.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}

			String token = authHeader.substring(7);
			if (jwtService.isTokenValid(token, userDetails)) {
				return ResponseEntity.ok().build();
			}
		} catch (Exception e) {
			log.debug("Falha na validação do token", e);
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	// Método para logout (opcional - depende da estratégia de invalidação de
	// tokens)
	@PostMapping("/logout")
	@Operation(summary = "Invalidar token JWT")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		// Implementação depende da estratégia de logout (blacklist, etc.)
		return ResponseEntity.ok().build();
	}
}