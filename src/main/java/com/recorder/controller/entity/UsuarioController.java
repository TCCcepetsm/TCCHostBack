package com.recorder.controller.entity; // Verifique se este é o pacote correto para seus controllers

import com.recorder.dto.UsuarioDTO; // O DTO que você usa no seu serviço
import com.recorder.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Indica que esta classe é um controller REST
@RequestMapping("/api/usuarios") // Define o caminho base para todos os endpoints neste controller
@RequiredArgsConstructor // Gera um construtor com os campos 'final' para injeção de dependência
@Tag(name = "Usuários", description = "Gerenciamento de usuários da aplicação") // Anotação para documentação
																				// Swagger/OpenAPI
public class UsuarioController {

	private final UsuarioService usuarioService; // Injeção de dependência do serviço de usuário

	/**
	 * Endpoint para registrar um novo usuário no sistema.
	 * Recebe um UsuarioDTO no corpo da requisição e delega ao serviço.
	 * Retorna o usuário cadastrado com status 201 Created.
	 */
	@PostMapping("/cadastro") // Define o endpoint POST para /api/usuarios/cadastro
	@ResponseStatus(HttpStatus.CREATED) // Retorna status HTTP 201 Created em caso de sucesso
	@Operation(summary = "Registra um novo usuário", description = "Cria uma nova conta de usuário no sistema.", responses = {
			@ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou senhas não conferem", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"As senhas não coincidem\"}"))),
			@ApiResponse(responseCode = "409", description = "Email ou CPF já cadastrados", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Email já cadastrado\"}")))
	})
	public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
		// O @Valid garante que as validações definidas no UsuarioDTO (ex: @NotBlank,
		// @Email)
		// serão aplicadas automaticamente antes do método ser executado.
		Usuario novoUsuario = usuarioService.registrar(usuarioDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
	}

	// Você pode adicionar mais endpoints CRUD aqui no futuro, como:
	// GET /api/usuarios/{id}
	// PUT /api/usuarios/{id}
	// DELETE /api/usuarios/{id}
	// GET /api/usuarios
}